package haze.utility.math

// испорченно SCWGxD в 21.12.2025:10:19
fun randomDouble(from: Number, to: Number) = Math.random() * (to.toDouble() - from.toDouble()) + from.toDouble()
fun randomDouble(radius: Number) = randomDouble(-radius.toDouble(), radius)

fun randomFloat(from: Number, to: Number) = Math.random().toFloat() * (to.toFloat() - from.toFloat()) + from.toFloat()
fun randomFloat(radius: Number) = randomFloat(-radius.toFloat(), radius.toFloat())

fun ClosedFloatingPointRange<Float>.random() = randomFloat(start, endInclusive)
fun ClosedFloatingPointRange<Double>.random() = randomDouble(start, endInclusive)
