package day11

import helper.Debug
import helper.pairwise
import helper.point.long.LongPoint
import helper.point.base.Point

fun solve(text: String, debug: Debug = Debug.Disabled, factor: Long = 2): Long {
    debug { println("Starting calculation with $factor") }
    val lines = text.lines()

    val galaxies = lines.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c == '#') {
                Point(x, y)
            } else {
                null
            }
        }
    }

    val maxX = galaxies.maxOf { it.x }
    val maxY = galaxies.maxOf { it.y }

    val emptyColums = (0..maxX).filter { x -> galaxies.none { it.x == x } }
    val emptyRows = (0..maxY).filter { y -> galaxies.none { it.y == y } }

    debug {
        println("Empty Columns: $emptyColums")
        println("Empty Rows: $emptyRows")
        println("Galaxies Before (${galaxies.size}): $galaxies")
    }

    val expandedGalaxies = expandGalaxy(galaxies, emptyColums, emptyRows, factor)

    debug {
        println("Expanded (${expandedGalaxies.size}): $expandedGalaxies")
    }

    var count = 0
    return expandedGalaxies.pairwise(expandedGalaxies).sumOf { (a, b) ->
            val abs = (a - b).abs()
            debug { println("Match ${++count} $a to $b = $abs") }
            abs
    }

}

private fun expandGalaxy(
    galaxies: List<Point>,
    emptyColums: List<Int>,
    emptyRows: List<Int>,
    factor: Long
) = galaxies.map { (x, y) ->
    val xLt = emptyColums.count { it < x } * (factor - 1)
    val yLt = emptyRows.count { it < y } * (factor - 1)
    val point = LongPoint(x + xLt, y + yLt)

    point
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    return solve(text, debug, 1000000)
}
