package haze.module

import haze.key.KeySaveLoadAble
import haze.module.impl.combat.*
import haze.module.impl.combat.AttackAura
import haze.module.impl.connection.PingSpoof
import haze.module.impl.misc.*
import haze.module.impl.move.*
import haze.module.impl.player.*
import haze.module.impl.visual.*
import haze.module.impl.world.FastBreak
import haze.module.impl.world.FastPlace
import haze.module.impl.world.Fucker
import haze.module.impl.world.PacketMine
import haze.module.impl.world.Phase
import haze.module.impl.world.Scaffold
import haze.setting.SaveLoadAble
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

// created by dicves_recode on 30.11.2025
@Suppress("UNUSED_EXPRESSION")
object Modules : SaveLoadAble, KeySaveLoadAble {
    private val mutableList = mutableSetOf<Module>()

    val list: Set<Module>
        get() = mutableList

    internal fun register(module: Module) {
        mutableList.add(module)
    }

    fun getModule(name: String) = list.find { it.name.equals(name, true) }

    init {
        // combat
        AimAssist
        AttackAura
        AutoAttack
        AutoClicker
        Criticals
        KeepSprint
        Range
        SprintReset

        // connect
        PingSpoof

        // misc
        AutoRegister
        Debug
        RPC
        Test

        // move
        AirStuck
        AutoSprint
        FastLadder
        Fly
        LongJump
        NoJumpDelay
        NoSlow
        Speed
        Velocity

        // player
        AutoPotion
        AutoTool
        ChestStealer
        FastUse
        InventoryCleaner
        NoFall
        Timer

        // visual
        AspectRatio
        ClientSettingGUI
        ESP
        FreeLook
        HitAccuracy
        InstantItemSwitch
        Tracers
        Vignette

        // world
        FastBreak
        FastPlace
        Fucker
        PacketMine
        Phase
        Scaffold
    }

    override var json: JsonObject
        get() = buildJsonObject {
            list.forEach {
                put(it.name, it.json)
            }
        }
        set(value) {
            list.forEach {
                it.json = value[it.name]?.jsonObject ?: return
            }
        }

    override var keyJson: JsonObject
        get() = buildJsonObject {
            list.forEach {
                put(it.name, it.keybind.json)
            }
        }
        set(value) {
            list.forEach {
                it.keybind.json = value[it.name]?.jsonObject ?: return
            }
        }
}
