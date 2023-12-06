package day6

import helper.Debug

//What do you get if you multiply these numbers together?
fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val races = text.lines().map { it.split((" +".toRegex())).drop(1)}

    var solution = 1
    for (rid in races[0].indices) {
        val time = races[0][rid].toLong()
        val recordDistance = races[1][rid].toLong()
        val winCount = getWinCount(time, recordDistance)

        solution *= winCount
    }

    return solution
}

private fun getWinCount(time: Long, recordDistance: Long) = (0 until time).count { t ->
    val remainingTime = time - t
    val distance = remainingTime * t
    distance > recordDistance
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val race = text.lines().map { it.replace(" ", "").split(":").last()}.map { it.toLong() }

    return getWinCount(race[0], race[1])
}
