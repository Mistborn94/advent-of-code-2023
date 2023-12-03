package day2

import helper.Debug

val pattern = "Game (\\d+): (.+)".toRegex()

fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    return text.lines()
        .map {
            val (_, id, game) = pattern.matchEntire(it)!!.groupValues
            id to game
        }
        .filter { (_, game) ->
            val (red, blue, green) = gameMinColours(game.trim())
            red <= 12 && (green <= 13) && blue <= 14
        }
        .sumOf { (key, _) -> key.toInt() }
}

private fun gameMinColours(game: String): Triple<Int, Int, Int> {
    var red = 0
    var blue = 0
    var green = 0

    game.split(";").flatMap { it.split(", ") }.forEach() {

        val (count, colour) = it.trim().split(" ")
        val toInt = count.toInt()
        when (colour) {
            "blue" -> if (toInt > blue) blue = toInt
            "red" -> if (toInt > red) red = toInt
            "green" -> if (toInt > green) green = toInt
            else -> throw IllegalArgumentException("Unknown colour $colour")
        }
    }
    return Triple(red, blue, green)
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    return text.lines()
        .map {
            val (_, id, game) = pattern.matchEntire(it)!!.groupValues
            id to game
        }
        .sumOf { (_, game) ->
            val (red, blue, green) = gameMinColours(game.trim())
            red * green * blue
        }
}
