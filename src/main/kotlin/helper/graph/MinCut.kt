package helper.graph


/**
 * Karger's algorithm for minimum cut
 * https://www.geeksforgeeks.org/introduction-and-implementation-of-kargers-algorithm-for-minimum-cut/
 *
 * This algorithm has random components, it needs to be run it multiple times to find the real minimum
 */
fun minCutKarger(
    connections: Map<String, Set<String>>,
    expectedCutSize: Int = Int.MAX_VALUE
): Pair<String, String>? {
    val graphCopy = connections.mapValuesTo(mutableMapOf()) { (_, v) -> v.associateWithTo(mutableMapOf()) { 1 } }
    return minCutKarger(graphCopy, expectedCutSize)
}

@JvmName("minCutKargerInternal")
private tailrec fun minCutKarger(graph: MutableMap<String, MutableMap<String, Int>>, expectedCutSize: Int): Pair<String, String>? {

    val first = graph.keys.random()
    val second = graph[first]!!.keys.random()
    val new = "$first-$second"

    val firstRemaining =  graph.remove(first)!!.filterKeys { it != second  }
    val secondRemaining =  graph.remove(second)!!.filterKeys { it != first  }
    val newConnections = (firstRemaining.keys + secondRemaining.keys).associateWithTo(mutableMapOf()) {
        (firstRemaining[it] ?: 0) + (secondRemaining[it] ?: 0)
    }

    graph.forEach { (_, v) ->
        val firstCount = v.remove(first) ?: 0
        val secondCount = v.remove(second) ?: 0
        if (firstCount > 0 || secondCount > 0) {
            v[new] = firstCount + secondCount
        }
    }
    graph[new] = newConnections

    return if (graph.size > 2) {
        minCutKarger(graph, 3)
    } else {
        val edgeCount = graph.entries.sumOf { (_, v) -> v.values.sum() } / 2
        if (edgeCount <= expectedCutSize) {
            return graph.keys.first() to graph.keys.last()
        } else {
            null
        }
    }
}
