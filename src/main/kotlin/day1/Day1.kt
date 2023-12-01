package day1

import helper.Debug

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    return text.lines().sumOf { "${it.first(Char::isDigit)}${it.last(Char::isDigit)}".toInt() }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    return text.lines().sumOf { calibrationNumber(it) }
}

val numberValues = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

private fun calibrationNumber(line: String): Int {
    val (_, firstLocation) = line.findAnyOf(numberValues.keys + numberValues.values)!!
    val (_, lastLocation) = line.findLastAnyOf(numberValues.keys + numberValues.values)!!


    val first = if (firstLocation[0].isDigit()) {
        firstLocation
    } else {
        numberValues[firstLocation]!!
    }

    val second = if (lastLocation[0].isDigit()) {
        lastLocation
    } else {
        numberValues[lastLocation]!!
    }

    return "$first$second".toInt()
}
