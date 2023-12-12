package day12

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val map = parseInput(text)

    return solve(map, debug)
}

private fun solve(
    map: List<Pair<String, List<Int>>>,
    debug: Debug,
): Long {
    return map.sumOf { (line, numbers) ->
        val pattern = solutionRegex(numbers)
        debug {
            println("========================")
            println("Evaluating $line [$pattern]")
        }
        val possibleArrangements = solveNewGroup(line, numbers, mutableMapOf(), debug, "")
        debug {
            println("Possible Arrangements is $possibleArrangements")
        }
        possibleArrangements
    }
}

private fun parseInput(text: String): List<Pair<String, List<Int>>> {
    val map = text.lines().map { line ->
        val split = line.split(" ")
        val numbers = split[1].split(",").map { it.toInt() }
        split[0] to numbers
    }
    return map
}

private fun solutionRegex(numbers: List<Int>) =
    numbers.joinToString(separator = "\\.+", prefix = "^\\.*", postfix = "\\.*$") { "\\#".repeat(it) }.toRegex()

fun solveNewGroup(
    chars: String,
    numbers: List<Int>,
    cache: MutableMap<Pair<String, List<Int>>, Long> = mutableMapOf(),
    debug: Debug,
    indent: String
): Long {
    debug { println("$indent├ Solving new group \"$chars\" $numbers") }
    return cache[chars to numbers] ?: if (chars.isEmpty()) {
        if (numbers.isEmpty()) {
            debug { println("$indent├ Good group - both empty") }
            1L
        } else {
            debug { println("$indent├ Bad group - numbers not empty") }
            0L
        }
    } else {
        val newIndent = "$indent│"
        when (chars.first()) {
            '.' -> solveNewGroup(chars.drop(1), numbers, cache, debug, newIndent)
            '#' -> solveCurrentGroup(chars, numbers, cache, debug, newIndent).also {
                debug { println("$indent├ Persisting entry \"$chars\" $numbers $it") }
                cache[chars to numbers] = it
            }

            '?' -> {

                debug { println("$indent├ Starting Unknown \"$chars\" $numbers") }
                val currentGroup = solveCurrentGroup(chars, numbers, cache, debug, newIndent)
                val newGroup = solveNewGroup(chars.drop(1), numbers, cache, debug, newIndent)
                debug { println("$indent├ Finished Unknown \"$chars\" $numbers - $newGroup + $currentGroup") }
                (currentGroup + newGroup).also {
                    debug { println("$indent├ Persisting entry \"$chars\" $numbers $it") }
                    cache[chars to numbers] = it
                }
            }

            else -> throw IllegalArgumentException("Bad input ${chars.first()}")
        }
    }
}

fun solveCurrentGroup(
    chars: String,
    numbers: List<Int>,
    cache: MutableMap<Pair<String, List<Int>>, Long>,
    debug: Debug,
    indent: String
): Long {
    val newIndent = "$indent│"
    debug { println("$indent├ Solving current group \"$chars\" $numbers") }
    return if (numbers.isEmpty()) {
        debug { println("$indent├ Bad solution $chars $numbers") }
        0
    } else {
        val groupSize = numbers.first()
        return when {
            chars.length < groupSize -> {
                debug { println("$indent├ Bad solution $chars $groupSize") }
                0
            }

            chars.length == groupSize -> {
                if (numbers.size == 1 && chars.all { it != '.' }) {
                    debug { println("$indent├ Valid solution \"$chars\" $groupSize") }
                    1
                } else {
                    debug { println("$indent├ Bad solution $chars $groupSize") }
                    0
                }
            }

            else -> {
                val substring = chars.substring(0, groupSize)
                if (substring.all { it != '.' } && chars[groupSize] != '#') {
                    debug { println("$indent├ Good Group [$groupSize] [$substring${chars[groupSize]}] - $chars ") }
                    solveNewGroup(chars.drop(groupSize + 1), numbers.drop(1), cache, debug, newIndent)
                } else {
                    debug { println("$indent├ Bad Group [$groupSize] [$substring${chars[groupSize]}] - $chars ") }
                    0
                }
            }
        }
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val map = parseInput(text).map { (line, numbers) ->
        val newLine = Array(5) { line }.joinToString(separator = "?")
        val newNumbers = Array(5) { numbers }.flatMap { it }

        newLine to newNumbers
    }
    return solve(map, debug)
}
