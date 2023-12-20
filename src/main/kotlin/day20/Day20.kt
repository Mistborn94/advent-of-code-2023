package day20

import helper.Debug
import helper.lowestCommonMultiple

fun solveA(text: String, debug: Debug = Debug.Disabled, buttonCount: Int = 1000): Int {

    val modules: Map<String, Module> = parseInput(text)
    val outputModule = Module.OutputModule
    val scores = mutableMapOf(
        true to 0,
        false to 0
    )
    runSimulation(buttonCount, modules, outputModule, scores)

    val trueScore = scores[true] ?: 0
    val falseScore = (scores[false] ?: 0) + buttonCount
    debug {
        println(trueScore)
        println(falseScore)
    }
    return trueScore * falseScore
}

private fun runSimulation(
    buttonCount: Int,
    modules: Map<String, Module>,
    outputModule: Module.OutputModule,
    scores: MutableMap<Boolean, Int>
) {
    repeat(buttonCount) {
        val signals = mutableListOf(Triple("button", "broadcaster", false))
        while (signals.isNotEmpty()) {
            val (previous, name, signal) = signals.removeFirst()
            val module = modules.getOrDefault(name, outputModule)

            val output = module.acceptPulse(signal, previous)

            if (output != null) {
                scores.computeIfPresent(output) { _, v -> v + module.outputs.size }
                signals.addAll(module.outputs.map { Triple(name, it, output) })
            }
        }
    }
}

//Flip-flop modules (prefix %)
//Conjunction modules (prefix &)
//high = true

fun solveB(text: String, debug: Debug = Debug.Enabled): Long {
    val modules: Map<String, Module> = parseInput(text)
    val inverseConnections = modules.flatMap { (key, value) -> value.outputs.map { key to it } }.groupBy({ it.second }, { it.first })

    val defaultModule = Module.OutputModule
    return buttonPressesNeeded("rx", false, modules, inverseConnections)!!
}

val cache = mutableMapOf<String, Long>()

fun buttonPressesNeeded(
    name: String,
    finalPulse: Boolean,
    modules: Map<String, Module>,
    inverseConnections: Map<String, List<String>>,
): Long? {
    val module = modules[name] ?: Module.OutputModule
    val inputs = inverseConnections[name] ?: emptyList()

    return cache[name] ?: when (module) {
        is Module.ConjunctionModule -> {
            if (finalPulse) {
                inputs.minOfWith(comparator) { buttonPressesNeeded(it, false, modules, inverseConnections) }
            } else {
                val answer: Long = if (inputs.all { modules[it] is Module.FlipFlopModule }) {
                   inputs.sumOf { buttonPressesNeeded(it, true, modules, inverseConnections)!! }
                } else {
                    inputs.fold(1) { acc, previous ->
                        val previousAnswer =
                            buttonPressesNeeded(previous, true, modules, inverseConnections)

                        lowestCommonMultiple(acc, previousAnswer!!)
                    }
                }
                cache[name] = answer
                answer
            }
        }

        is Module.Broadcaster -> if (finalPulse) null else 1
        is Module.FlipFlopModule -> if (inputs.size == 1) {
            val multiplier = if (finalPulse) 1 else 2
            buttonPressesNeeded(inputs[0], false, modules, inverseConnections)?.let { it * multiplier }
        } else if (inputs.size == 2 && inputs.count { modules[it] is Module.ConjunctionModule } == 1) {
            val multiplier = if (finalPulse) 1 else 2
            val boringInput = inputs.first { modules[it] !is Module.ConjunctionModule }
            val conjunctionInput = inputs.first { modules[it] is Module.ConjunctionModule }

            if (conjunctionInput !in cache) {
                buttonPressesNeeded(boringInput, false, modules, inverseConnections)?.let { it * multiplier }
            } else {
               throw IllegalStateException("Shouldn't get here")
            }
        } else {
            throw IllegalStateException("Shouldn't get here")
        }

        is Module.OutputModule -> inputs.minOfWith(comparator) {
            buttonPressesNeeded(it, finalPulse, modules, inverseConnections)
        }
    }
}


val comparator = Comparator.nullsLast<Long>(Comparator.naturalOrder())


private fun parseInput(text: String): Map<String, Module> {
    val connections = text.lines().associate { line ->
        val (left, right) = line.split(" -> ")

        val connections = right.split(", ").toSet()
        if (left.startsWith("&") || left.startsWith("%")) {
            left.substring(1) to (left[0] to connections)
        } else {
            left to (null to connections)
        }
    }

    val modules: Map<String, Module> = connections.map { (name, value) ->
        val (moduleType, moduleOutputs) = value
        if (moduleType == '%') {
            name to Module.FlipFlopModule(moduleOutputs)
        } else if (name == "broadcaster") {
            name to Module.Broadcaster(moduleOutputs)
        } else if (moduleType == '&') {
            val possibleInputs = connections.filterValues { (_, connections) ->
                connections.contains(name)
            }.keys
            name to Module.ConjunctionModule(possibleInputs, moduleOutputs)
        } else {
            throw IllegalArgumentException("Unknown type $name")
        }
    }.toMap()
    return modules
}

sealed class Module(val outputs: Set<String>) {


    open fun reset() {}

    abstract fun acceptPulse(pulse: Boolean, from: String): Boolean?

    class Broadcaster(outputs: Set<String>) : Module(outputs) {

        override fun acceptPulse(pulse: Boolean, from: String): Boolean = pulse
    }

    class FlipFlopModule(outputs: Set<String>) : Module(outputs) {
        private var state = false

        override fun reset() {
            state = false
        }

        override fun acceptPulse(pulse: Boolean, from: String): Boolean? {
            if (!pulse) {
                state = !state
                return state
            }
            return null
        }
    }

    class ConjunctionModule(val inputs: Set<String>, outputs: Set<String>) : Module(outputs) {
        val size = inputs.size
        val memory = inputs.associateWith { false }.toMutableMap()

        override fun reset() {
            inputs.forEach {
                memory[it] = false
            }
        }

        override fun acceptPulse(pulse: Boolean, from: String): Boolean {
            memory[from] = pulse
            return !memory.values.all { it }
        }
    }

    data object OutputModule : Module(emptySet()) {
        override fun acceptPulse(pulse: Boolean, from: String): Boolean? = null
    }
}
