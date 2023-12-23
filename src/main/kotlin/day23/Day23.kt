package day23

import helper.Debug
import helper.graph.longestPathDfs
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get

//paths (.), forest (#), and steep slopes (^, >, v, and <).
fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val start = Point(map[0].indexOf('.'), 0)
    val end = Point(map.last().indexOf('.'), map.lastIndex)

    val graph = buildGraph(map, start, end, true)
    return longestPathDfs(graph, start, end)
}

fun nextPointsA(point: Point, map: List<String>): List<Point> {
    return if (point.y == -1) {
        listOf(point + Point(0, 1))
    } else when (map[point]) {
        '.' -> point.neighbours()
        '<' -> listOf(Point(point.x - 1, point.y))
        '>' -> listOf(Point(point.x + 1, point.y))
        '^' -> listOf(Point(point.x, point.y - 1))
        'v' -> listOf(Point(point.x, point.y + 1))
        else -> {
            println("Unexpected path char ${map[point]}")
            emptyList()
        }
    }.filter { it in map && map[it] != '#' }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val map = text.lines()
    val start = Point(map[0].indexOf('.'), 0)
    val end = Point(map.last().indexOf('.'), map.lastIndex)

    val graph = buildGraph(map, start, end, false)
    return longestPathDfs(graph, start, end)
}

fun buildGraph(
    map: List<String>,
    start: Point,
    end: Point,
    part1: Boolean
): Map<Point, Map<Point, Int>> {
    val graph = mutableMapOf<Point, MutableMap<Point, Int>>()
    populateIntersection(map, start, end, graph, part1)
    return graph
}

fun populateIntersection(
    map: List<String>,
    current: Point,
    mapEnd: Point,
    graph: MutableMap<Point, MutableMap<Point, Int>>,
    part1: Boolean
) {
    val currentNeighbours = nextPointsA(current, map)
    val paths = currentNeighbours.mapNotNull {
        searchUntilIntersection(it, current, map, mapEnd)
    }
    graph[current] = paths.toMap(mutableMapOf())

    paths.forEach { (point, _) ->
        if (point !in graph) {
            populateIntersection(map, point, mapEnd, graph, part1)
        }
    }

    if (!part1) {
        paths.forEach { (end, cost) ->
            graph[end]?.put(current, cost)
        }
    }
}

fun searchUntilIntersection(
    first: Point,
    previous: Point,
    map: List<String>,
    mapEnd: Point
): Pair<Point, Int>? {
    val currentPath = mutableSetOf(previous)
    var current = first
    var neighbours = nextPointsA(current, map).filter { it !in currentPath }
    while (neighbours.size == 1) {
        currentPath.add(current)
        current = neighbours.first()
        neighbours = nextPointsA(current, map).filter { it !in currentPath }
    }

    return if (neighbours.isNotEmpty() || current == mapEnd) {
        current to currentPath.size
    } else {
        null
    }
}