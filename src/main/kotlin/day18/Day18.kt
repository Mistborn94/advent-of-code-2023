package day18

import helper.Debug
import helper.point.*
import helper.point.long.LongPoint
import helper.point.long.LongVertexPolygon
import helper.point.long.toLong

val pattern = """([RLDU]) (\d+) \(#([a-z0-9]{5})([a-z0-9])\)""".toRegex()
fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val points = parsePoints(text) { parseLine1(it) }
    return LongVertexPolygon(points).areaWithBoundary()
}

fun parsePoints(text: String, parser: (String) -> Pair<Direction, Long>): MutableList<LongPoint> {
    var currentPoint = LongPoint(0, 0)
    var borderSize = 0L
    val points = mutableListOf<LongPoint>()
    text.lines().forEach { line ->
        val (direction, stepCount) = parser(line)
        points.add(currentPoint)
        borderSize += stepCount
        currentPoint = getNextPoint(currentPoint, direction, stepCount)
    }

    return points
}

private fun parseLine1(line: String): Pair<Direction, Long> {
    val (directionChar, stepCount) = pattern.matchEntire(line)!!.destructured
    val direction = when (directionChar) {
        "R" -> Direction.RIGHT
        "L" -> Direction.LEFT
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        else -> throw IllegalArgumentException("Unknown direction $directionChar")
    }

    val stepCountLong = stepCount.toLong()
    return Pair(direction, stepCountLong)
}


private fun parseLine2(line: String): Pair<Direction, Long> {
    val (_, _, hexSteps, hexDirection) = pattern.matchEntire(line)!!.destructured
    val direction = when (hexDirection) {
        "0" -> Direction.RIGHT
        "1" -> Direction.DOWN
        "2" -> Direction.LEFT
        "3" -> Direction.UP
        else -> throw IllegalArgumentException("Unknown direction $hexDirection")
    }
    val stepCountLong = hexSteps.toLong(16)
    return Pair(direction, stepCountLong)
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val points = parsePoints(text) { parseLine2(it) }
    return LongVertexPolygon(points).areaWithBoundary()
}

private fun getNextPoint(
    currentPoint: LongPoint,
    direction: Direction,
    stepCount: Long
) = currentPoint + direction.point.toLong() * stepCount

