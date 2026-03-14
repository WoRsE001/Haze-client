package haze.utility.entity

import net.minecraft.world.entity.Entity
import kotlin.math.sqrt

// created by dicves_recode on 28.12.2025
fun Entity.distanceTo(x: Double, y: Double, z: Double) =
    sqrt(distanceToSqr(x, y, z))
