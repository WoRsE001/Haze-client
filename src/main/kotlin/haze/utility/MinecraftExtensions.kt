package haze.utility

import haze.HazeClient
import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.systems.GpuDevice
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component

val Window.dimensions
    get() = intArrayOf(width, height)

val Window.scaledDimension
    get() = intArrayOf(guiScaledWidth, guiScaledHeight)

val mc: Minecraft
    inline get() = Minecraft.getInstance()
val player: LocalPlayer
    inline get() = mc.player!!
val level: ClientLevel
    inline get() = mc.level!!
val connection: ClientPacketListener
    inline get() = mc.connection!!
val gameMode: MultiPlayerGameMode
    inline get() = mc.gameMode!!
val gpuDevice: GpuDevice
    inline get() = RenderSystem.getDevice()

private var timer_ = 1f

var Minecraft.timer: Float
    get() = timer_
    set(value) { timer_ = value }

fun nullCheck() = mc.player != null && mc.level != null

fun Minecraft.sendMessage(message: String) {
    this.gui.chat.addMessage(Component.literal("§e[" + HazeClient.NAME + "]§f " + message))
}


