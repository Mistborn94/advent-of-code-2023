package day9

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val histories = text.lines().map { line -> line.split(" ").map { it.toInt() } }
    return histories.sumOf { extrapolateNext(it) }
}

fun extrapolateNext(history: List<Int>): Int {
    val differences = history.windowed(2).map { (a, b) -> b - a }
    return if (differences.all { it == differences[0] }) {
        history.last() + differences[0]
    } else {
        history.last() + extrapolateNext(differences)
    }
}

fun extrapolatePrevious(history: List<Int>): Int {
    val differences = history.windowed(2).map { (a, b) -> b - a }
    return if (differences.all { it == differences[0] }) {
        history.first() - differences[0]
    } else {
        history.first() - extrapolatePrevious(differences)
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val histories = text.lines().map { line -> line.split(" ").map { it.toInt() } }
    return histories.sumOf { extrapolatePrevious(it) }
}
