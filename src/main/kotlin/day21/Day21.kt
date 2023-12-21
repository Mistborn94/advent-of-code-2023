package day21

import helper.Debug
import helper.point.base.Point
import helper.point.base.contains
import helper.point.base.get
import helper.point.base.indexOf

fun solveA(text: String, debug: Debug = Debug.Disabled, steps: Int = 64): Long {
    val map = text.lines()
    val start = map.indexOf('S')

    var frontier = setOf(start)
    var previous = emptySet<Point>()

    val stepsModulo = steps % 2
    var count = 1L - stepsModulo
    repeat(steps) { i ->
        val current = frontier
        frontier = frontier.flatMapTo(mutableSetOf()) { f ->
            f.neighbours().filter { n -> n !in previous && n in map && map[n] != '#' }
        }
        previous = current

        val iteration = i + 1
        debug {
            val visualized =
                map.mapIndexed { y, line -> line.mapIndexed { x, c -> if (Point(x, y) in frontier) 'O' else c } }
            println("Steps $iteration - Inner count $count")
            println(visualized.joinToString(separator = "\n") { line -> line.joinToString(separator = "") })
        }
        if (iteration % 2 == stepsModulo) {
            count += frontier.size
        }


    }
    debug {
        println("Frontier: $frontier")
    }
    return count
}

fun solveB(text: String, debug: Debug = Debug.Disabled, steps: Int): Long {
    debug {
        println("Starting with $steps")
    }
    val map = text.lines()
    val originalSize = map.size
    val bounds = Point(originalSize, originalSize)
    val originalStart = map.indexOf('S')
    var frontier = setOf(originalStart)
    var count = 0L

    var i = 1
    var previous = emptySet<Point>()
    val found = mutableListOf<Long>()

    while (found.size < 3) {
        val current = frontier
        frontier = frontier.flatMapTo(mutableSetOf()) {
            it.neighbours().filter { n -> n !in previous && map[n % bounds] != '#' }
        }
        previous = current

        if (i % 2 == 1) {
            count += frontier.size
        }

        if (i % 262 == 1) {
            found.add(count)
        }
        i++
    }

    val target = steps / 262 + 1
    var (a, b, c) = found
    var iteration = 4
    while (iteration <= target) {
        val d1 = b - a
        val d2 = c - b
        val d3 = d2 - d1

        a = b
        b = c
        c += d3 + d2
        debug { println("$iteration $c") }
        iteration++
    }

    return c
}