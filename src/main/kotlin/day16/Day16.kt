package day16

import helper.Debug
import helper.point.Direction
import helper.point.Point
import helper.point.contains
import helper.point.get

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val entryPoint = Point(0, 0) to Direction.RIGHT
    return solve(entryPoint, map)
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val possiblyEntryPoints = map[0].indices.map { Point(it, 0) to Direction.DOWN } +
            map.last().indices.map { Point(it, map.lastIndex) to Direction.UP } +
            map.indices.map { Point(0, it) to Direction.RIGHT } +
            map.indices.map { Point(map[0].lastIndex, it) to Direction.LEFT }
    debug { println("Count = ${possiblyEntryPoints.size}") }
    return possiblyEntryPoints.maxOf {
        debug {
            println("Solving for $it")
        }
        solve(it, map)
    }
}

private fun solve(
    entryPoint: Pair<Point, Direction>,
    map: List<String>
): Int {
    val toVisit = mutableSetOf(entryPoint)

    val energizedCells = mutableSetOf<Point>()
    val visited = mutableSetOf<Pair<Point, Direction>>()

    while (toVisit.isNotEmpty()) {
        val pair = toVisit.removeFirst()

        if (pair in visited) {
            continue
        }
        visited.add(pair)

        var (currentPosition, currentDirection) = pair

        while (currentPosition in map) {
            energizedCells.add(currentPosition)
            val cell = map[currentPosition]
            currentDirection = when (cell) {
                '.' -> currentDirection
                '/' -> when (currentDirection) {
                    Direction.LEFT -> Direction.DOWN
                    Direction.DOWN -> Direction.LEFT
                    Direction.RIGHT -> Direction.UP
                    Direction.UP -> Direction.RIGHT
                }

                '\\' -> when (currentDirection) {
                    Direction.LEFT -> Direction.UP
                    Direction.UP -> Direction.LEFT
                    Direction.RIGHT -> Direction.DOWN
                    Direction.DOWN -> Direction.RIGHT
                }

                '|' -> when (currentDirection) {
                    Direction.DOWN, Direction.UP -> currentDirection
                    Direction.LEFT, Direction.RIGHT -> {
                        toVisit.add(currentPosition to Direction.DOWN)
                        Direction.UP
                    }
                }

                '-' -> when (currentDirection) {
                    Direction.DOWN, Direction.UP -> {
                        toVisit.add(currentPosition to Direction.LEFT)
                        Direction.RIGHT
                    }

                    Direction.LEFT, Direction.RIGHT -> currentDirection
                }

                else -> throw IllegalArgumentException("""Unknown char "$cell"""")
            }

            currentPosition += currentDirection.pointPositiveDown
        }
    }

    return energizedCells.size
}

private fun <E> MutableSet<E>.removeFirst(): E {
    val first = first()
    remove(first)
    return first
}

