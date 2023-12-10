package helper.point

import helper.size

data class Rectangle(val xRange: IntRange, val yRange: IntRange) {
    operator fun contains(point: Point) = point.x in xRange && point.y in yRange

    fun area(): Int = xRange.size() * yRange.size()
}