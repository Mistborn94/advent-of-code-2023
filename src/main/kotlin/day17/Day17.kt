package day17

import helper.Debug
import helper.graph.GraphSearchResult
import helper.graph.findShortestPathByPredicate
import helper.point.Direction
import helper.point.Point
import helper.point.contains
import helper.point.get
import kotlin.math.min

data class PointInDirection(val point: Point, val direction: Direction, val line: Int) {
    fun neighbours(): List<PointInDirection> {
        return buildList {
            if (line < 3) {
                add(PointInDirection(point + direction.pointPositiveDown, direction, line + 1))
            }
            add(PointInDirection(point + direction.right.pointPositiveDown, direction.right, 1))
            add(PointInDirection(point + direction.left.pointPositiveDown, direction.left, 1))
        }
    }

    fun ultraNeighbours(): List<PointInDirection> {
        return buildList {
            if (line < 10) {
                add(PointInDirection(point + direction.pointPositiveDown, direction, line + 1))
            }
            if (line >= 4) {
                add(PointInDirection(point + direction.right.pointPositiveDown, direction.right, 1))
                add(PointInDirection(point + direction.left.pointPositiveDown, direction.left, 1))
            }
        }
    }
}

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().map { line -> line.map { it.digitToInt() } }
    val start = PointInDirection(Point(0, 0), Direction.RIGHT, 0)
    val end = Point(map[0].lastIndex, map.lastIndex)

    val path = findShortestPathByPredicate(
            start,
            { (p, _) -> p == end },
            { it.neighbours().filter { (n) -> n in map } },
            { _, (point) -> map[point] })
    return path.getScore()
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines().map { line -> line.map { it.digitToInt() } }
    val pathA = searchWithStartDirection(map, Direction.DOWN)
    val pathB = searchWithStartDirection(map,  Direction.RIGHT)
    return min(pathA.getScore(), pathB.getScore())
}

private fun searchWithStartDirection(
    map: List<List<Int>>,
    startDirection: Direction
): GraphSearchResult<PointInDirection> {
    val start = PointInDirection(Point(0, 0), startDirection, 0)
    val end = Point(map[0].lastIndex, map.lastIndex)
    return findShortestPathByPredicate(
        start,
        { (p, _, line) -> p == end && line >= 4 },
        { it.ultraNeighbours().filter { (p) -> p in map } },
        { _, (point) -> map[point] }
    )
}
