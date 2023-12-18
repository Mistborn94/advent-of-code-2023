package helper.point.double

import kotlin.math.absoluteValue

class DoubleVertexPolygon(val points: List<DoublePoint>) {
    fun areaWithBoundary() = area() + perimiter() / 2 + 1
    fun areaInsideBoundary() = area() - perimiter() / 2 + 1

    fun perimiter(): Double {
        val workingList = points + points.first()

        return workingList.zipWithNext().sumOf { (a, b) -> (b - a).abs() }
    }

    fun area(): Double {
        val workingList = points + points.first()

        return workingList.zipWithNext().sumOf { (a, b) ->
            a.x * b.y - a.y * b.x
        }.absoluteValue / 2
    }
}
