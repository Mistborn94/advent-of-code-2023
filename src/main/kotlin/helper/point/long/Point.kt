package helper.point.long

import helper.point.base.Point

data class LongPoint(val x: Long, val y: Long) {

    fun abs(): Long {
        return kotlin.math.abs(x) + kotlin.math.abs(y)
    }

    operator fun minus(other: LongPoint): LongPoint = LongPoint(x - other.x, y - other.y)
    operator fun plus(other: LongPoint) = LongPoint(x + other.x, y + other.y)
    operator fun times(value: Long) = LongPoint(x * value, y * value)

    fun neighbours() = listOf(
        LongPoint(x + 1, y),
        LongPoint(x - 1, y),
        LongPoint(x, y + 1),
        LongPoint(x, y - 1)
    )

    fun diagonalNeighbours() = listOf(
        LongPoint(x + 1, y + 1),
        LongPoint(x + 1, y - 1),
        LongPoint(x - 1, y + 1),
        LongPoint(x - 1, y - 1)
    )

    /**
     * Rotate the point clockwise around the origin
     */
    fun clockwise(degrees: Int): LongPoint {
        return when (degrees) {
            90 -> LongPoint(y, -x)
            180 -> LongPoint(-x, -y)
            270 -> LongPoint(-y, x)
            else -> throw UnsupportedOperationException()
        }
    }

    /**
     * Rotate the point counterclockwise around the origin
     */
    fun counterClockwise(degrees: Int): LongPoint = clockwise(360 - degrees)

    companion object {
        val ZERO = LongPoint(0, 0)
    }
}

fun Point.toLong() = LongPoint(x.toLong(), y.toLong())
