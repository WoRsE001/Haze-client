package haze.module.impl.world

import haze.event.Event
import haze.event.impl.JumpEvent
import haze.event.impl.LegitClickTimingEvent
import haze.event.impl.MovementInputEvent
import haze.event.impl.TickEvent
import haze.mixin.accessor.MinecraftAccessor
import haze.module.Category
import haze.module.Module
import haze.module.impl.world.scaffold.*
import haze.utility.mc
import haze.utility.player
import haze.utility.math.roundTo
import haze.utility.nullCheck
import haze.utility.player.airTicks
import haze.utility.player.groundTicks
import haze.utility.player.hasXZMotion
import haze.utility.player.isMoving
import haze.utility.player.rotation.CameraRotation
import haze.utility.player.rotation.RotateAble
import haze.utility.player.rotation.Rotation
import haze.utility.player.rotation.rotate
import haze.utility.player.rotation.rotation
import haze.utility.player.inventory.slot.resetSlot
import haze.utility.level
import haze.setting.preset.MoveCorrector
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.BlockItem
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

// испорченно SCWGxD в 22.12.2025:19:33
object Scaffold : Module(
    "Scaffold",
    Category.WORLD
), RotateAble {
    // rotation

    private val rotations = group("Rotations")

        // pitch

        private val pitch = rotations.group("Pitch")

            private val pitchSpeed by pitch.number("Speed", 180.0, 0.0..180.0, 1.0)
            private val staticPitchOnMove by pitch.boolean("Static on move", false)
            private val pitchSortMode = pitch.list("Sort mode").apply {
                choice(Lowest)
                choice(Highest)
                choice(Mid)
                choice(Nearest).select()
            }
            private val sameY by pitch.boolean("Same Y", false)

        // yaw

        private val yaw = rotations.group("Yaw")

            private val yawSpeed by yaw.number("Speed", 180.0, 0.0..180.0, 1.0)
            private val correctWithMovement by yaw.boolean("Correct with movement", true)
            private val offsetYaw by yaw.number("Offset", 0.0, 0.0..50.0, 1.0)
            private val roundYaw by yaw.boolean("Round", true)
            private val roundYawFactor by yaw.number("Round factor", 45.0, 1.0..180.0, 1.0).visible { roundYaw }
            private val nearestYaw by yaw.boolean("Nearest", false)
            private val nearestYawOnlyOnClutch by yaw.boolean("Only on clutch", false).visible { nearestYaw }

        // telly

        private val telly = rotations.toggleAbleGroup("Telly", false)

            private val backPitchSpeed by telly.number("Back yaw speed", 180.0, 0.0..180.0, 1.0)
            private val backYawSpeed by telly.number("Back pitch speed", 180.0, 0.0..180.0, 1.0)
            private val tellyGroundTicks by telly.number("Ground ticks", 0.0, 0.0..10.0, 1.0)
            private val tellyAirTicks by telly.number("Air ticks", 0.0, 0.0..10.0, 1.0)

    // movement

    private val movement = group("Movement")

        private val autoJump by movement.boolean("Auto jump", false)
        private val towerMode = movement.list("Tower mode")
        private val towerModeNone by towerMode.choice("None").select()
        private val towerModeJump = towerMode.choice("Jump")
        private val jumpHeight by number("Jump height", 0.42, 0.0..2.0, 0.01).visible { towerModeJump.selected() }
        private val moveCorrect = movement.tree(MoveCorrector())


    private var targetBlock: BlockPos? = null

    override val rotateOrdinal = 0

    init {
        registerToRotationHandler()
    }

    override fun onDisable() {
        resetSlot()
    }

    override fun onEvent(event: Event) {
        when (event) {
            JumpEvent.Pre -> {
                if (willTower() && towerModeJump.selected())
                    JumpEvent.Pre.height = jumpHeight.toFloat()
            }

            LegitClickTimingEvent -> {
                val slot = getSlot()

                if (slot != -1)
                    player.inventory.selectedSlot = slot

                val hitResult = mc.hitResult

                if (hitResult is BlockHitResult && hitResult.blockPos == targetBlock && (hitResult.direction != Direction.UP || !sameY()))
                    (mc as MinecraftAccessor).invokeSartUseItem()
            }

            is MovementInputEvent -> {
                event.jump = (autoJump || mc.options.keyJump.isDown) && (!telly.toggled || player.groundTicks > tellyGroundTicks)
            }

            TickEvent.Pre -> {
                targetBlock = getBestBlock()
            }
        }

        targetBlock?.let {
            moveCorrect.correctMove(event)
        }
    }

    override fun rotate() {
        val rotation = getRotation()

        if (willTower()) {
            rotation.pitch = 90f
        }

        val delta = rotation - player.rotation

        if (isTelly())
            delta.limit(backPitchSpeed.toFloat(), backYawSpeed.toFloat())
        else
            delta.limit(pitchSpeed.toFloat(), yawSpeed.toFloat())

        delta.fix()
        player.rotate(delta)
    }

    override fun shouldRotate() = this.toggled && nullCheck() && targetBlock != null

    private fun getBestBlock(): BlockPos? {
        val playerPos = player.blockPosition()
        var bestBlock: BlockPos? = null

        for (x in playerPos.x - 5..playerPos.x + 5) {
            for (y in playerPos.y - 3..<playerPos.y) {
                for (z in playerPos.z - 5..playerPos.z + 5) {
                    val blockPos = BlockPos(x, y, z)

                    if (level.isEmptyBlock(blockPos))
                        continue

                    if (bestBlock == null ||
                        player.distanceToSqr(Vec3(x + 0.5, y + 0.5, z + 0.5)) <
                        player.distanceToSqr(Vec3(bestBlock.x + 0.5, bestBlock.y + 0.5, bestBlock.z + 0.5))
                    ) bestBlock = blockPos
                }
            }
        }

        return bestBlock
    }

    private fun getRotation(): Rotation {
        var yaw = getYaw(CameraRotation.yaw, correctWithMovement, isTelly())
        val roundedYaw = yaw.toDouble().roundTo(roundYawFactor).toFloat()

        if (roundYaw) {
            yaw = roundedYaw
        }

        if (roundedYaw.toInt() % 90 == 0 && !isTelly()) {
            val isOnRightSide =
                floor(player.position().x + cos(Math.toRadians(yaw.toDouble())) * 0.5) != floor(player.position().x) ||
                        floor(player.position().z + sin(Math.toRadians(yaw.toDouble())) * 0.5) != floor(
                    player.position().z
                )

            yaw += offsetYaw.toFloat() * if (isOnRightSide) 1 else -1
        }

        if (nearestYaw && !isTelly())
            return getNearestRotation(targetBlock!!, yaw, pitchSortMode.get() as PitchSortMode)

        val pitch = if (staticPitchOnMove && player.isMoving())
            player.xRot
        else
            getNormalPitch(targetBlock!!, yaw, pitchSortMode.get() as PitchSortMode)

        return Rotation(pitch, yaw)
    }

    private fun getSlot(): Int {
        for (i in 0..8) {
            val itemStack = player.inventory.getItem(i)

            if (itemStack.isEmpty)
                continue

            if (itemStack.item is BlockItem)
                return i
        }

        return -1
    }

    private fun isTelly() = telly.toggled && (if (player.onGround()) player.groundTicks >= tellyGroundTicks else player.airTicks < tellyAirTicks)

    private fun sameY() = sameY && !mc.options.keyUse.isDown

    private fun willTower() = !sameY() && !player.hasXZMotion()
}