package day13

import helper.Debug
import helper.point.points
import kotlin.math.min

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val patterns = text.replace("\r", "").split("\n\n")
    return patterns.sumOf { pattern ->
        debug { println("Pattern:\n $pattern") }
        findReflectionScore(pattern.trim().lines())!!
    }
}

private fun findReflectionScore(lines: List<String>, forbiddenScore: Int? = null): Int? {
    val columnReflectRange = 0 until lines[0].lastIndex
    var possibleVerticals: Set<Int> = mutableSetOf<Int>().apply {
        addAll(1..lines[0].lastIndex)
    }

    for (row in 0 until lines.lastIndex) {
        val returnValue = checkHorizontal(row, lines)

        if (returnValue != null && returnValue != forbiddenScore) {
            return returnValue
        }

        if (possibleVerticals.isNotEmpty()) {
            possibleVerticals = checkVerticals(columnReflectRange, lines[row], possibleVerticals)
        }

    }
    if (possibleVerticals.isNotEmpty()) {
        possibleVerticals = checkVerticals(columnReflectRange, lines.last(), possibleVerticals)
    }


    return possibleVerticals.firstOrNull { it != forbiddenScore }
}

private fun checkHorizontal(row: Int, lines: List<String>): Int? {
    val toIndex = row + 1
    val before = lines.subList(0, toIndex).reversed()
    val after = lines.subList(toIndex, lines.size)

    val size = min(before.size, after.size)

    val returnValue = if (before.subList(0, size) == after.subList(0, size)) {
        100 * toIndex
    } else {
        null
    }
    return returnValue
}

private fun checkVerticals(
    columnReflectRange: IntRange,
    line: String,
    possibleVerticals: Set<Int>
): Set<Int> {
    val possibleColumnReflections = mutableSetOf<Int>()
    for (c in columnReflectRange) {
        val endIndex = c + 1
        val before = line.substring(0, endIndex).reversed()
        val after = line.substring(endIndex)

        if (after == before || (after.length > before.length && after.startsWith(before)) || (before.length > after.length && before.startsWith(
                after
            ))
        ) {
            possibleColumnReflections.add(endIndex)
        }
    }
    return possibleVerticals.intersect(possibleColumnReflections)
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val patterns = text.replace("\r", "").split("\n\n")
    return patterns.sumOf { pattern ->
        val lines = pattern.trim().lines()
        val oldReflectionLine = findReflectionScore(lines)!!
        val firstNotNullOfOrNull = mutatePattern(
            debug,
            lines
        ).firstNotNullOfOrNull { findReflectionScore(it, oldReflectionLine) }

        if (firstNotNullOfOrNull == null) {
            println("ERROR! - Old Reflection line is $oldReflectionLine")
            println("Original Pattern is:\n${lines.joinToString(separator = "\n")}")
        }
        firstNotNullOfOrNull!!
    }
}

fun mutatePattern(debug: Debug, lines: List<String>): Sequence<List<String>> {
    debug {
        println(
            "Original Pattern:\n${
                lines.joinToString(
                    separator = "\n",
                    postfix = "\n-----------------------"
                )
            }"
        )
    }

    return lines.points().asSequence().map { (x, y) ->

        val rowsBefore = lines.subList(0, y)
        val current = lines[y]
        val rowsAfter = lines.subList(y + 1, lines.size)
        val replacementChar = if (current[x] == '.') "#" else "."
        val withReplaced = current.replaceRange(x..x, replacementChar)

        debug { println("Mutating $x $y to $replacementChar") }

        val newPattern = buildList {
            addAll(rowsBefore)
            add(withReplaced)
            addAll(rowsAfter)
        }
        debug { println(newPattern.joinToString(separator = "\n", postfix = "\n-----------------------")) }
        newPattern
    }
}
