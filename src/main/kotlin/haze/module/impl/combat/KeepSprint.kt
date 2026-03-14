package haze.module.impl.combat

import haze.module.Category
import haze.module.Module

// испорченно SCWGxD в 29.12.2025:22:11
object KeepSprint : Module(
    "KeepSprint",
    Category.COMBAT
) {
    private val ground = group("Ground")
    val groundMotion by ground.number("Motion", 1.0, 0.0..1.0, 0.01)
    val groundSprint by ground.boolean("Sprint", true)

    private val air = group("Air")
    val airMotion by air.number("Motion", 1.0, 0.0..1.0, 0.01)
    val airSprint by air.boolean("Sprint", true)
}