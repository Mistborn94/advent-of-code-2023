package day10

import helper.Debug
import helper.pad
import helper.point.base.Point
import helper.point.base.Polygon
import helper.point.base.get
import kotlin.collections.*
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
    val toVisit = mutableListOf<Pair<Point, Point>>()
    toVisit.addAll(startingNeighbours.map { it to startingPosition })

    do {
        val (currentPoint, previousPoint) = toVisit.removeFirst()
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

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().pad('.')
    val graph = buildGraph(map)

    val startingPosition = graph.keys.first { map[it] == 'S' }
    val (loopPath, _) = buildLoop(startingPosition, graph)

    return Polygon(loopPath).areaInsideBoundary()
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
