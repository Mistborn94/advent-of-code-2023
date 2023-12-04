package day3

import helper.Debug
import helper.point.Point

data class PartNumber(val y: Int, val xRange: IntRange, val value: Int) {
    val possibleSymbols: List<Point>
        get() {
            val startX = xRange.first - 1
            val endx = xRange.last + 1

            return (startX..endx).flatMap { listOf(Point(it, y - 1), Point(it, y + 1)) } + Point(
                startX,
                y
            ) + Point(endx, y)
        }
}

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val parts = mutableListOf<PartNumber>()
    val symbols = mutableSetOf<Point>()
    text.lines().forEachIndexed { y, line ->
        var xStart: Int? = null
        var xEnd = 0
        var numberValue = ""

        line.forEachIndexed { x, c ->
            if (!c.isDigit()) {
                if (xStart != null) {
                    parts.add(PartNumber(y, xStart!!..xEnd, numberValue.toInt()))
                    xStart = null
                    xEnd = 0
                    numberValue = ""
                }
            }

            when {
                c == '.' -> {}

                c.isDigit() -> {
                    if (xStart == null) {
                        xStart = x
                    }
                    numberValue += c
                    xEnd = x
                }

                else -> symbols.add(Point(x, y))
            }
        }

        if (xStart != null) {
            parts.add(PartNumber(y, xStart!!..xEnd, numberValue.toInt()))
            xStart = null
            xEnd = 0
            numberValue = ""
        }
    }

    debug{ println("Shapes $symbols")}

    val filtered = parts.filter { part ->
        val possibleSymbols = part.possibleSymbols
        debug{ println("Possible symbols for $part is $possibleSymbols")}
        possibleSymbols.any { it in symbols }
    }

    println("Parts Count ${parts.size}")
    println("Filtered ${filtered.size}")

    return filtered.sumOf { it.value }

}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val parts = mutableListOf<PartNumber>()
    val stars = mutableSetOf<Point>()
    text.lines().forEachIndexed { y, line ->
        var xStart: Int? = null
        var xEnd = 0
        var numberValue = ""

        line.forEachIndexed { x, c ->
            if (!c.isDigit()) {
                if (xStart != null) {
                    parts.add(PartNumber(y, xStart!!..xEnd, numberValue.toInt()))
                    xStart = null
                    xEnd = 0
                    numberValue = ""
                }
            }

            when {
                c.isDigit() -> {
                    if (xStart == null) {
                        xStart = x
                    }
                    numberValue += c
                    xEnd = x
                }
                c == '*' -> stars.add(Point(x, y))
            }
        }

        if (xStart != null) {
            parts.add(PartNumber(y, xStart!!..xEnd, numberValue.toInt()))
            xStart = null
            xEnd = 0
            numberValue = ""
        }
    }

    val gears = stars.sumOf { star ->
        val adjacentParts = parts.filter { it.possibleSymbols.contains(star) }
        if (adjacentParts.size == 2) {
            adjacentParts[0].value * adjacentParts[1].value
        } else {
            0
        }
    }
    return gears

}
