package day10

import helper.Debug
import helper.point.Direction
import helper.point.DirectionPoints
import helper.point.Point
import helper.point.Rectangle
import java.util.*

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().pad('.')
    val graph = map.flatMapIndexed { y: Int, line: String ->
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

    val startingPosition = graph.keys.first { map[it] == 'S' }
    //build loop
    val (_, loopDistances) = buildLoop(startingPosition, graph)
    return loopDistances.values.max()
}

fun buildLoop(
    startingPosition: Point,
    graph: Map<Point, Set<Point>>
): Pair<List<Point>, Map<Point, Int>> {

    val startingNeighbours = startingPosition.neighbours().filter { startingPosition in (graph[it] ?: emptySet()) }
    val distances = mutableMapOf(startingPosition to 0)
    val neighbours = mutableMapOf<Point, Set<Point>>()
    val toVisit = LinkedList<Pair<Point, Set<Point>>>()
    toVisit.addAll(startingNeighbours.map { it to setOf(startingPosition) })
    var loopPath: SequencedSet<Point>? = null

    do {
        val (nextPoint, seenPath) = toVisit.pop()
        val existingDistance = distances[nextPoint]
        if (existingDistance == null || existingDistance > seenPath.size) {
            distances[nextPoint] = seenPath.size
        }

        val allNeighbours = graph[nextPoint] ?: emptySet()
        val newPath = LinkedHashSet<Point>().apply {
            addAll(seenPath)
            add(nextPoint)
        }
        if (allNeighbours.contains(startingPosition) && seenPath.size > 1) {
            loopPath = newPath
        } else {
            val possibleNeighbours = allNeighbours.filter { it !in seenPath }

            toVisit.addAll(possibleNeighbours.map { it to newPath })
        }
    } while (!toVisit.isEmpty() && loopPath == null)

    return (loopPath!!.toList() + startingPosition) to distances.filterKeys { it in loopPath }

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
    val graph = map.flatMapIndexed { y: Int, line: String ->
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

    val startingPosition = graph.keys.first { map[it] == 'S' }
    val (loopPath, _) = buildLoop(
        startingPosition,
        graph
    )

    val pathSteps =
        loopPath.windowed(2, partialWindows = false).associate { (first, second) -> first to second }.toMap()
    val mapBounds = Rectangle(0..map[0].length, 0..map.size)

    val toVisitPoints = LinkedHashSet<Point>()
    toVisitPoints.add(Point.ZERO)
    val followPath = LinkedHashSet<Pair<Point, Direction>>()

    var lastDirection = Direction.UP
    var nextPosition = startingPosition
    val leftPoints = mutableSetOf<Point>()
    val rightPoints = mutableSetOf<Point>()
    do {
        val current = nextPosition
        nextPosition = pathSteps[current]!!

        val directionPoints = DirectionPoints.downPositive
        val direction = Direction.entries.first {
            current + directionPoints[it] == nextPosition
        }

        if (direction != lastDirection) {
            val leftPoint = current + directionPoints[lastDirection.left]
            if (leftPoint !in pathSteps) {
                leftPoints.add(leftPoint)
            }
            val rightPoint = current + directionPoints[lastDirection.right]
            if (rightPoint !in pathSteps) {
                rightPoints.add(rightPoint)
            }
        }

        val leftPoint = current + directionPoints[direction.left]
        if (leftPoint !in pathSteps) {
            leftPoints.add(leftPoint)
        }
        val rightPoint = current + directionPoints[direction.right]
        if (rightPoint !in pathSteps) {
            rightPoints.add(rightPoint)
        }

        followPath.add(current to direction)
        lastDirection = direction

    } while (nextPosition != startingPosition)

    expandArea(leftPoints, mapBounds, pathSteps)
    expandArea(rightPoints, mapBounds, pathSteps)

    return if (leftPoints.contains(Point.ZERO)) {
        debug { println(getFilteredMap(map, rightPoints, leftPoints, pathSteps)) }
        rightPoints.size
    } else {
        debug { println(getFilteredMap(map, leftPoints, rightPoints, pathSteps)) }
        leftPoints.size
    }
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

private fun List<Int>.toWallRanges(y: Int, graph: Map<Point, Set<Point>>): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    val toVisit = toMutableList()

    while (toVisit.isNotEmpty()) {
        val start = toVisit.removeFirst()
        var current = start

        while (toVisit.isNotEmpty() && toVisit.first() == current + 1) {
            val currentPoint = Point(current, y)
            val nextPoint = Point(toVisit.first(), y)
            if (nextPoint in (graph[currentPoint] ?: emptySet())) {
                current = toVisit.removeFirst()
            } else {
                break
            }
        }
        ranges.add(start..current)
    }
    return ranges
}
