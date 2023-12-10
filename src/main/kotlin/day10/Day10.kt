package day10

import helper.Debug
import helper.point.Direction
import helper.point.DirectionPoints
import helper.point.Point
import helper.point.Rectangle
import java.util.*
import kotlin.math.max

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().pad('.')
    val graph = buildGraph(map)
    val startingPosition = graph.keys.first { map[it] == 'S' }
    val (_, loopDistances) = buildLoop(startingPosition, graph)
    return loopDistances
}

fun buildLoop(
    startingPosition: Point,
    graph: Map<Point, Set<Point>>
): Pair<List<Point>, Int> {

    val startingNeighbours = startingPosition.neighbours().filter { startingPosition in (graph[it] ?: emptySet()) }
    val pathToStart = mutableMapOf<Point, Point>()
    val toVisit = LinkedList<Pair<Point, Point>>()
    toVisit.addAll(startingNeighbours.map { it to startingPosition })

    do {
        val (currentPoint, previousPoint) = toVisit.pop()
        if (currentPoint !in pathToStart) {
            pathToStart[currentPoint] = previousPoint

            val allNeighbours = graph[currentPoint] ?: emptySet()
            val possibleNeighbours = allNeighbours.filter { it != previousPoint }
            toVisit.addAll(possibleNeighbours.map { it to currentPoint })
        } else {
            val path = buildPath(currentPoint, startingPosition, pathToStart)
            val otherPath = buildPath(previousPoint, startingPosition, pathToStart)

            val loopPath = otherPath.reversed() + path
            val maxDistance = max(path.size, otherPath.size) - 1
            return loopPath to maxDistance
        }
    } while (true)
}

private fun buildPath(
    previousPoint: Point,
    startingPosition: Point,
    pathToStart: MutableMap<Point, Point>
): MutableList<Point> {
    val otherPath = mutableListOf(previousPoint)
    while (otherPath.last() != startingPosition) {
        otherPath.add(pathToStart[otherPath.last()]!!)
    }
    return otherPath
}

operator fun List<String>.get(point: Point) = this[point.y][point.x]

private fun List<String>.pad(c: Char): List<String> {
    val length = this[0].length + 2
    val str = buildString(length) {
        repeat(length) { append(c) }
    }
    return listOf(str) + this.map { "$c$it$c" } + listOf(str)
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().pad('.')
    val graph = buildGraph(map)

    val startingPosition = graph.keys.first { map[it] == 'S' }
    val (loopPath, _) = buildLoop(startingPosition, graph)

    val pathSteps =
        loopPath.windowed(2, partialWindows = false).associate { (first, second) -> first to second }.toMap()
    val mapBounds = Rectangle(0..map[0].length, 0..map.size)

    var lastDirection = direction(startingPosition, pathSteps[startingPosition]!!)
    var nextPosition = startingPosition
    val leftPoints = mutableSetOf<Point>()
    val rightPoints = mutableSetOf<Point>()

    do {
        val current = nextPosition
        nextPosition = pathSteps[current]!!

        val direction = direction(current, nextPosition)

        if (direction != lastDirection) {
            val leftPoint = leftPoint(current, lastDirection)
            if (leftPoint !in pathSteps) {
                leftPoints.add(leftPoint)
            }
            val rightPoint = rightPoint(current, lastDirection)
            if (rightPoint !in pathSteps) {
                rightPoints.add(rightPoint)
            }
        }

        val leftPoint = leftPoint(current, direction)
        if (leftPoint !in pathSteps) {
            leftPoints.add(leftPoint)
        }
        val rightPoint = rightPoint(current, direction)
        if (rightPoint !in pathSteps) {
            rightPoints.add(rightPoint)
        }

        lastDirection = direction

    } while (nextPosition != startingPosition)

    expandArea(leftPoints, mapBounds, pathSteps)
    expandArea(rightPoints, mapBounds, pathSteps)

    return if (leftPoints.contains(Point.ZERO)) {
        debug { println(getFilteredMap(map, rightPoints, leftPoints, pathSteps).joinToString(separator = "\n")) }
        rightPoints.size
    } else {
        debug { println(getFilteredMap(map, leftPoints, rightPoints, pathSteps).joinToString(separator = "\n")) }
        leftPoints.size
    }
}

private fun buildGraph(map: List<String>) = map.flatMapIndexed { y: Int, line: String ->
    line.mapIndexedNotNull { x, c ->
        val position = Point(x, y)
        when (c) {
            '.' -> null
            '|' -> position to listOf(Point(x, y - 1), Point(x, y + 1)).filter { map[it] != '.' }.toSet()
            '-' -> position to listOf(Point(x - 1, y), Point(x + 1, y)).filter { map[it] != '.' }.toSet()
            'S' -> position to emptySet()
            'J' -> position to listOf(Point(x, y - 1), Point(x - 1, y)).filter { map[it] != '.' }.toSet()
            'L' -> position to listOf(Point(x, y - 1), Point(x + 1, y)).filter { map[it] != '.' }.toSet()
            '7' -> position to listOf(Point(x, y + 1), Point(x - 1, y)).filter { map[it] != '.' }.toSet()
            'F' -> position to listOf(Point(x, y + 1), Point(x + 1, y)).filter { map[it] != '.' }.toSet()
            else -> throw IllegalArgumentException("$c")
        }
    }
}.toMap()

private fun rightPoint(current: Point, direction: Direction) = current + DirectionPoints.downPositive[direction.right]
private fun leftPoint(current: Point, direction: Direction) = current + DirectionPoints.downPositive[direction.left]

private fun direction(
    current: Point,
    nextPosition: Point
) = Direction.entries.first {
    current + DirectionPoints.downPositive[it] == nextPosition
}

private fun expandArea(
    area: MutableSet<Point>,
    mapBounds: Rectangle,
    filteredGraph: Map<Point, Point>
) {

    val toVisit = LinkedHashSet<Point>()
    toVisit.addAll(area)

    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        area.add(current)

        toVisit.addAll(current.neighbours().filter { it in mapBounds && it !in filteredGraph && it !in area })
    }
}

private fun getFilteredMap(
    map: List<String>,
    insidePoints: MutableSet<Point>,
    outsidePoints: MutableSet<Point>,
    loop: Map<Point, Point>
) = map.mapIndexed { y: Int, s: String ->
    s.mapIndexed { x, c ->
        if (Point(x, y) in insidePoints) {
            'I'
        } else if (Point(x, y) in outsidePoints) {
            'O'
        } else if (Point(x, y) in loop) {
            c
        } else {
            '.'
        }
    }.joinToString(separator = "")
}

