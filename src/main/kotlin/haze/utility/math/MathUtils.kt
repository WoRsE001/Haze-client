package haze.utility.math

import java.math.BigDecimal
import java.math.RoundingMode

// created by dicves_recode on 01.12.2025
fun Float.roundTo(step: Float): Float {
    val bdNumber = BigDecimal.valueOf(this.toDouble())
    val bdStep = BigDecimal.valueOf(step.toDouble())
    val divided = bdNumber.divide(bdStep, 0, RoundingMode.HALF_UP)
    return divided.multiply(bdStep).toFloat()
}

fun Float.roundTo(step: Double): Float {
    val bdNumber = BigDecimal.valueOf(this.toDouble())
    val bdStep = BigDecimal.valueOf(step)
    val divided = bdNumber.divide(bdStep, 0, RoundingMode.HALF_UP)
    return divided.multiply(bdStep).toFloat()
}

fun Double.roundTo(step: Float): Double {
    val bdNumber = BigDecimal.valueOf(this)
    val bdStep = BigDecimal.valueOf(step.toDouble())
    val divided = bdNumber.divide(bdStep, 0, RoundingMode.HALF_UP)
    return divided.multiply(bdStep).toDouble()
}

fun Double.roundTo(step: Double): Double {
    val bdNumber = BigDecimal.valueOf(this)
    val bdStep = BigDecimal.valueOf(step)
    val divided = bdNumber.divide(bdStep, 0, RoundingMode.HALF_UP)
    return divided.multiply(bdStep).toDouble()
}

fun lerp(factor: Float, valueMin: Float, valueMax: Float): Float = (valueMax - valueMin) * factor + valueMin

fun lerp(factor: Double, valueMin: Double, valueMax: Double): Double = (valueMax - valueMin) * factor + valueMin

fun map(value: Float, valueMin: Float, valueMax: Float, resultMin: Float, resultMax: Float) =
    lerp((value - valueMin) / (valueMax - valueMin), resultMin, resultMax)

fun map(value: Double, valueMin: Double, valueMax: Double, resultMin: Double, resultMax: Double) =
    lerp((value - valueMin) / (valueMax - valueMin), resultMin, resultMax)

fun Float.toDegrees() = this / Math.PI.toFloat() * 180f