package day25

import helper.Debug
import helper.graph.minCutKarger

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
        answer = minCutKarger(connections, 3)?.let { (first, second) ->
            val firstCount = first.count { it == '-' } + 1
            val secondCount = second.count { it == '-' } + 1
            firstCount * secondCount
        }
    } while (answer == null)
    return answer
}

