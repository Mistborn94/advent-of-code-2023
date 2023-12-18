package helper.point.long

import helper.point.base.Point
import helper.size
import kotlin.math.absoluteValue

data class LongRectangle(val xRange: LongRange, val yRange: LongRange) {
    operator fun contains(point: Point) = point.x in xRange && point.y in yRange

    fun area(): Long = xRange.size() * yRange.size()
}

class LongVertexPolygon(val points: List<LongPoint>) {
    fun areaWithBoundary() = area() + perimiter() / 2 + 1
    fun vertexCountInsideBoundary() = area() - perimiter() / 2 + 1

    fun perimiter(): Long {
        val workingList = points + points.first()

        return workingList.zipWithNext().sumOf { (a, b) -> (b - a).abs() }
    }

    fun area(): Long {
        val workingList = points + points.first()

        return workingList.zipWithNext().sumOf { (a, b) ->
            a.x * b.y - a.y * b.x
        }.absoluteValue / 2
    }
}



