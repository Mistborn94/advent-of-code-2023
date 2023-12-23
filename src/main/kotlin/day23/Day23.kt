package day23

import helper.Debug
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
): Map<Point,Map<Point, Int>> {
    val graph = mutableMapOf<Point, Map<Point, Int>>()
    populateIntersection(map, start, end, graph)

    return if (part1) {
        graph
    } else {
        val bidirectionalGraph = mutableMapOf<Point, MutableMap<Point, Int>>()
        bidirectionalGraph.putAll(graph.mapValues { (_, v) -> v.toMutableMap() })
        graph.forEach { (start, paths) ->
            paths.forEach { (end, cost) ->
                val endAsStart = bidirectionalGraph.getOrPut(end) { mutableMapOf() }
                endAsStart[start] = cost
            }
        }
        bidirectionalGraph
    }
}

fun populateIntersection(
    map: List<String>,
    intersection: Point,
    mapEnd: Point,
    graph: MutableMap<Point, Map<Point, Int>>
) {
    val currentNeighbours = nextPointsA(intersection, map)
    val paths = currentNeighbours.mapNotNull {
        searchUntilIntersection(it, intersection, map, mapEnd)
    }
    graph[intersection] = paths.toMap()

    paths.forEach { (point, _) ->
        if (point !in graph) {
            populateIntersection(map, point, mapEnd, graph)
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


fun longestPathDfs(
    graph: Map<Point, Map<Point, Int>>,
    start: Point,
    end: Point,
    seen: List<Point> = emptyList()
): Int {
    return if (start == end) {
        0
    } else {
        val nextForbidden = seen + start
        val max = graph[start]!!.maxOfOrNull { (point, cost) ->
            if (point !in seen) {
                longestPathDfs(graph, point, end, nextForbidden) + cost
            } else {
                Int.MIN_VALUE
            }
        } ?: Int.MIN_VALUE
        max
    }
}

