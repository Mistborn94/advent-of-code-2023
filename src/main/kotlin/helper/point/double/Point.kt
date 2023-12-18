package helper.point.double

import helper.point.base.Point

data class DoublePoint(val x: Double, val y: Double) {

    fun abs() = kotlin.math.abs(x) + kotlin.math.abs(y)
    operator fun minus(other: DoublePoint): DoublePoint = DoublePoint(x - other.x, y - other.y)
    operator fun plus(other: DoublePoint) = DoublePoint(x + other.x, y + other.y)
    operator fun times(value: Double): DoublePoint = DoublePoint(x * value, y * value)
}

fun Point.toDouble() = DoublePoint(x.toDouble(), y.toDouble())