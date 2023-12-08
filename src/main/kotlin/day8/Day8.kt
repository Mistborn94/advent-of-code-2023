package day8

import helper.Debug
import helper.lowestCommonMultiple

val regex = "([0-9A-Z]+) = \\(([0-9A-Z]+), ([0-9A-Z]+)\\)".toRegex()
fun solveA(
    text: String,
    debug: Debug = Debug.Disabled,
): Int {
    val lines = text.lines()
    val steps = lines[0].trim()
    val graph = parseGraph(lines)

    var i = 0
    var stepsCount = 0
    var currentNode = "AAA"

    while (currentNode != "ZZZ") {
        currentNode = nextNode(graph, currentNode, steps, i++ % steps.length)
        stepsCount++
    }

    return stepsCount
}

private fun parseGraph(lines: List<String>) = lines.drop(2).associate {
    val matchEntire = regex.matchEntire(it)
    val (item, left, right) = matchEntire!!.destructured
    item to (left to right)
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val lines = text.lines()
    val steps = lines[0].trim()
    val graph = parseGraph(lines)

    val startingNodes = graph.keys.filter { it.endsWith("A") }

    val loops = startingNodes.associateWith { findLoopPath(graph, steps, it) }
    return loops.values.fold(1) { acc, loop -> lowestCommonMultiple(acc, loop.loopSize.toLong()) }
}

class LoopData(path: List<Pair<String, Int>>) {
    private val loopNode = path.last()
    private val loopStart = path.indexOf(loopNode)
    private val zIndex = path.indexOfLast { (key, _) -> key.endsWith("Z") }
    val loopSize = path.size - loopStart - 1

    init {
        if (path.count { (key, _) -> key.endsWith("Z") } > 1) {
            println("$loopNode : Possible Ends > 1, using last")
        }
        if (zIndex != loopSize) {
            throw IllegalArgumentException("Algo is gonna break - size & offset must match")
        }
    }
}

fun findLoopPath(graph: Map<String, Pair<String, String>>, steps: String, start: String): LoopData {
    val seenNodes = LinkedHashSet<Pair<String, Int>>()
    var currentNodeStep = start to 0
    var i = 0

    while (currentNodeStep !in seenNodes) {
        seenNodes.add(currentNodeStep)
        val nextNode = nextNode(graph, currentNodeStep.first, steps, i)
        i = (i + 1) % steps.length
        currentNodeStep = nextNode to i
    }

    return LoopData(seenNodes.toList() + currentNodeStep)
}

private fun nextNode(
    graph: Map<String, Pair<String, String>>,
    currentNode: String,
    steps: String,
    stepIndex: Int
): String {
    val currentNodePair = graph[currentNode]
    return when (val direction = steps[stepIndex]) {
        'L' -> currentNodePair!!.first
        'R' -> currentNodePair!!.second
        else -> throw IllegalArgumentException("Unknown direction [$direction]")
    }
}
