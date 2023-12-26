package day25

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val connections = mutableMapOf<String, MutableSet<String>>()
    val allWires = mutableSetOf<Pair<String, String>>()

    text.lines().forEach { line ->
        val (left, rightText) = line.split(": ")
        val rights = rightText.split(" ")
        connections.getOrPut(left) { mutableSetOf() }.addAll(rights)
        rights.forEach { right ->
            connections.getOrPut(right) { mutableSetOf() }.add(left)
            allWires.add(minOf(left, right) to maxOf(left, right))
        }
    }

    return kargerUntilFound(connections)
}

fun kargerUntilFound(connections: Map<String, Set<String>>): Int {
    var answer: Int?
    do {
        val graphCopy = connections.mapValuesTo(mutableMapOf()) { (_, v) -> v.associateWithTo(mutableMapOf()) { 1 } }
        answer = karger(graphCopy)
    } while (answer == null)
    return answer
}

/**
 * https://www.geeksforgeeks.org/introduction-and-implementation-of-kargers-algorithm-for-minimum-cut/
 */
tailrec fun karger(graph: MutableMap<String, MutableMap<String, Int>>): Int? {

    val first = graph.keys.random()
    val second = graph[first]!!.keys.random()
    val new = "$first-$second"

    val firstRemaining = graph[first]!!.filterKeys { it != second  }
    val secondRemaining = graph[second]!!.filterKeys { it != first  }
    val newConnections = (firstRemaining.keys + secondRemaining.keys).associateWithTo(mutableMapOf()) {
        (firstRemaining[it] ?: 0) + (secondRemaining[it] ?: 0)
    }
    graph.remove(first)
    graph.remove(second)

    graph.forEach { (_, v) ->
        val firstCount = v.remove(first) ?: 0
        val secondCount = v.remove(second) ?: 0
        if (firstCount > 0 || secondCount > 0) {
            v[new] = firstCount + secondCount
        }
    }
    graph[new] = newConnections

    return if (graph.size > 2) {
        karger(graph)
    } else {
        val edgeCount = graph.entries.sumOf { (_, v) -> v.values.sum() } / 2
        if (edgeCount <= 3) {
            val firstCount = graph.keys.first().count { it == '-' } + 1
            val secondCount = graph.keys.last().count { it == '-' } + 1
            firstCount * secondCount
        } else {
            null
        }
    }
}

