package day14

import helper.Debug
import helper.point.Direction
import helper.point.base.*


fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val lines = text.lines()
    val rockPositions = lines.findAll('O').toMutableSet()
    val mapBounds = Rectangle(lines[0].indices, lines.indices)

    moveInDirection(rockPositions, lines, Direction.NORTH, mapBounds)

    return rockPositions.sumOf { lines.size - it.y }
}

private fun moveInDirection(
    finalRockPositions: MutableSet<Point>,
    lines: List<String>,
    direction: Direction,
    mapBounds: Rectangle
) {
    val rockPositions = when (direction) {
        Direction.UP -> finalRockPositions.sortedBy { it.y }
        Direction.DOWN -> finalRockPositions.sortedByDescending { it.y }
        Direction.LEFT -> finalRockPositions.sortedBy { it.x }
        Direction.RIGHT -> finalRockPositions.sortedByDescending { it.x }
    }
    for (rock in rockPositions) {
        finalRockPositions.remove(rock)
        var current = rock
        var next = rock + direction.point
        while (next in mapBounds && lines[next] != '#' && !finalRockPositions.contains(next)) {
            current = next
            next += direction.point
        }
        finalRockPositions.add(current)
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val cycleCount = 1_000_000_000
    val lines = text.lines()
    val rockPositions = lines.findAll('O').toMutableSet()
    val mapBounds = Rectangle(lines[0].indices, lines.indices)

    val seenStates = mutableMapOf<Set<Point>, Int>()

    var cycle: Int = 0
    while (rockPositions !in seenStates) {
        seenStates[rockPositions.toSet()] = cycle

        moveInDirection(rockPositions, lines, Direction.NORTH, mapBounds)
        moveInDirection(rockPositions, lines, Direction.WEST, mapBounds)
        moveInDirection(rockPositions, lines, Direction.SOUTH, mapBounds)
        moveInDirection(rockPositions, lines, Direction.EAST, mapBounds)

        ++cycle
    }

    val seenAt = seenStates[rockPositions]!!
    val cycleLength = cycle - seenAt
    val remainder = (cycleCount - seenAt) % cycleLength

    repeat(remainder) {
        moveInDirection(rockPositions, lines, Direction.NORTH, mapBounds)
        moveInDirection(rockPositions, lines, Direction.WEST, mapBounds)
        moveInDirection(rockPositions, lines, Direction.SOUTH, mapBounds)
        moveInDirection(rockPositions, lines, Direction.EAST, mapBounds)
    }

    return rockPositions.sumOf { lines.size - it.y }
}
