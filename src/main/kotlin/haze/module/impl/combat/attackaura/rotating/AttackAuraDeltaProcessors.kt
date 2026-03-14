package haze.module.impl.combat.attackaura.rotating

import haze.utility.math.random
import haze.utility.math.randomFloat
import haze.utility.rotation.PlayerDeltaTracker
import haze.utility.rotation.Rotation

object ClampDeltaProcessor : AttackAuraDeltaProcessor("Clamp") {
    private val yLimit by numberRange("Y limit", 180.0..180.0, 0.0..180.0, 0.1)
    private val xLimit by numberRange("X limit", 180.0..180.0, 0.0..180.0, 0.1)

    override fun process(delta: Rotation): Rotation {
        return delta.limited(yLimit.random().toFloat(), xLimit.random().toFloat())
    }
}

object BasicRandomizeDeltaProcessor : AttackAuraDeltaProcessor("Basic randomize") {
    private val xStrength by number("Pitch strength", 1.0, 0.0..10.0, 0.1)
    private val yStrength by number("Yaw strength", 1.0, 0.0..10.0, 0.1)

    override fun process(delta: Rotation) =
        delta + Rotation(randomFloat(xStrength), randomFloat(yStrength))
}

object GcdFixDeltaProcessor : AttackAuraDeltaProcessor("Gcd fix") {
    override fun process(delta: Rotation) = delta.fixed()
}

object LinearSmoothDeltaProcessor : AttackAuraDeltaProcessor("Linear smooth") {
    private val xFactor by numberRange("Pitch factor", 1.0..1.5, 1.0..10.0, 0.01)
    private val yFactor by numberRange("Yaw factor", 1.0..1.5, 1.0..10.0, 0.01)

    override fun process(delta: Rotation) =
        delta / Rotation(xFactor.random().toFloat(), yFactor.random().toFloat())
}

object MixDeltaDeltaProcessor : AttackAuraDeltaProcessor("Mix delta") {
    private val xFactor by numberRange("Pitch factor", 0.3..0.7, 0.0..1.0, 0.01)
    private val yFactor by numberRange("Yaw factor", 0.3..0.7, 0.0..1.0, 0.01)

    override fun process(delta: Rotation) =
        PlayerDeltaTracker.delta.mixed(delta, Rotation(xFactor.random().toFloat(), yFactor.random().toFloat()))
}