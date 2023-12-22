package helper.point.base

import helper.size
import kotlin.math.absoluteValue

data class Rectangle(val xRange: IntRange, val yRange: IntRange, val label: String = "") {
    operator fun contains(point: Point) = point.x in xRange && point.y in yRange

    fun area(): Int = xRange.size() * yRange.size()
}

class Polygon(val points: Collection<Point>) {

    /**
     * Pick's theorem allows us to calculate the inside vertices and boundary inclusive area
     * https://en.wikipedia.org/wiki/Pick%27s_theorem
     */
    fun areaWithBoundary() = area() + perimiter() / 2 + 1
    fun areaInsideBoundary() = area() - perimiter() / 2 + 1

    fun perimiter(): Int {
        val workingList = points + points.first()

        return workingList.zipWithNext().sumOf { (a, b) -> (b - a).abs() }
    }

    /**
     * The shoelace algorithm can be used to calculate the area of a polygon
     */
    fun area(): Int {
        val workingList = points + points.first()

        return workingList.zipWithNext().sumOf { (a, b) ->
            a.x * b.y - (a.y) * b.x
        }.absoluteValue / 2
    }
}

@JvmName("strBounds")
fun List<String>.bounds(): Rectangle = Rectangle(this[0].indices, this.indices)
fun <E> List<Collection<E>>.bounds(): Rectangle = Rectangle(this[0].indices, this.indices)