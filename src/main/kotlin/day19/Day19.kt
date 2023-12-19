package day19

import helper.CompositeRange
import helper.Debug
import helper.addAllIf
import helper.removeFirst

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    operator fun get(char: Char): Int {
        return when (char) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException("Bad part $char")
        }
    }

    val total = x + m + a + s
}

data class PartRanges(
    val x: CompositeRange<Int>,
    val m: CompositeRange<Int>,
    val a: CompositeRange<Int>,
    val s: CompositeRange<Int>
) {
    operator fun get(char: Char): CompositeRange<Int> {
        return when (char) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> throw IllegalArgumentException("Bad part $char")
        }
    }

    fun minus(char: Char, range: IntRange): PartRanges {
        val charRange = get(char)
        return set(char, charRange - range)
    }

    fun overlap(char: Char, range: IntRange): PartRanges {
        val charRange = get(char)
        return set(char, charRange.overlap(range))
    }

    operator fun set(char: Char, range: CompositeRange<Int>): PartRanges {
        return when (char) {
            'x' -> copy(x = range)
            'm' -> copy(m = range)
            'a' -> copy(a = range)
            's' -> copy(s = range)
            else -> throw IllegalArgumentException("Bad part $char")
        }
    }

    fun isEmpty(): Boolean = x.isEmpty() || m.isEmpty() || a.isEmpty() || s.isEmpty()
    fun isNotEmpty(): Boolean = !isEmpty()
    fun combinations(): Long = x.size() * m.size() * a.size() * s.size()

    companion object {
        const val COMPONENT_MAX = 4000
        private val maxCompositeRange = CompositeRange.newIntRange(1..COMPONENT_MAX)

        val MAX = PartRanges(maxCompositeRange, maxCompositeRange, maxCompositeRange, maxCompositeRange)
    }
}


sealed class Rule(val component: Char, val output: String) {

    fun evaluate(part: Part): String? {
        val toEvaluate = part[component]

        return if (matches(toEvaluate)) {
            output
        } else {
            null
        }
    }

    abstract fun matches(value: Int): Boolean

    abstract fun matchedRange(): IntRange

    class LessThanRule(
        component: Char,
        private val limit: Int,
        output: String,
    ) : Rule(component, output) {
        override fun matches(value: Int): Boolean = value < limit
        override fun matchedRange(): IntRange = 1..<limit
        override fun toString(): String = "$component<$limit:$output"
    }

    class GreaterThanRule(
        component: Char,
        private val limit: Int,
        output: String,
    ) : Rule(component, output) {
        override fun matches(value: Int): Boolean = value > limit
        override fun matchedRange(): IntRange = limit + 1 ..PartRanges.COMPONENT_MAX
        override fun toString(): String = "$component>$limit:$output"
    }
}

class Workflow(val rules: List<Rule>, val defaultOutput: String) {
    fun evaluate(part: Part): String {
        return rules.firstNotNullOfOrNull { it.evaluate(part) } ?: defaultOutput
    }

    //Return accepted ranges and continued ranges
    fun ranges(inputRanges: PartRanges): Pair<List<PartRanges>, List<Pair<String, PartRanges>>> {
        val accepted = mutableListOf<PartRanges>()
        val continued = mutableListOf<Pair<String, PartRanges>>()

        val remainingRange = rules.fold(inputRanges) { range, rule ->
            val matchedRange = range.overlap(rule.component, rule.matchedRange())
            val unmatchedRange = range.minus(rule.component, rule.matchedRange())

            if (matchedRange.isNotEmpty()) {
                if (rule.output == "A") {
                    accepted.add(matchedRange)
                } else if (rule.output != "R") {
                    continued.add(rule.output to matchedRange)
                }
            }

            unmatchedRange
        }

        if (remainingRange.isNotEmpty()) {
            if (defaultOutput == "A") accepted.add(remainingRange)
            else if (defaultOutput != "R") continued.add(defaultOutput to remainingRange)
        }

        return accepted to continued
    }
}


val partRegex = """\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)}""".toRegex()
fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val (workflowsText, parts) = text.split("\n\n")

    val workflows = workflowsText.lines().associate { parseWorkflow(it) }

    var sum = 0
    parts.lines().forEach {
        val (x, m, a, s) = partRegex.matchEntire(it)!!.destructured
        val part = Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())

        val status = runWorkflows(part, workflows)
        if (status == "A") {
            debug { println("$part accepted") }
            sum += part.total
        } else {
            debug { println("$part rejected - $status") }
        }
    }
    return sum
}

fun runWorkflows(part: Part, workflows: Map<String, Workflow>): String {
    var lastOutcome = "in"
    do {
        val currentWorkflow = workflows[lastOutcome]!!
        lastOutcome = currentWorkflow.evaluate(part)
    } while (lastOutcome != "A" && lastOutcome != "R")

    return lastOutcome
}

fun parseWorkflow(line: String): Pair<String, Workflow> {
    val key = line.substringBefore("{")
    val rules = line.substring(key.length + 1, line.lastIndex).split(",")

    val defaultRule = rules.last()
    val matchingRules = rules.dropLast(1).map {
        val value = it.substring(2, it.indexOf(':'))
        val outcome = it.substringAfter(":")
        when (it[1]) {
            '<' -> Rule.LessThanRule(it.first(), value.toInt(), outcome)
            '>' -> Rule.GreaterThanRule(it.first(), value.toInt(), outcome)
            else -> throw IllegalArgumentException("Don't know ${it[1]}")
        }
    }

    return key to Workflow(matchingRules, defaultRule)
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val (workflowsText, _) = text.split("\n\n")

    val workflows = workflowsText.lines().associate { parseWorkflow(it) }

    return evaluateB(workflows)
}

fun evaluateB(workflows: Map<String, Workflow>): Long {
    val toVisit = mutableSetOf("in" to PartRanges.MAX)
    val acceptedRanges = mutableListOf<PartRanges>()

    while (toVisit.isNotEmpty()) {
        val (workflowName, ranges) = toVisit.removeFirst()
        val workflow = workflows[workflowName]!!
        val (acceptedByWorkflow, continuedPairs) = workflow.ranges(ranges)

        acceptedRanges.addAll(acceptedByWorkflow)
        toVisit.addAll(continuedPairs)
    }

    return acceptedRanges.sumOf { it.combinations() }
}
