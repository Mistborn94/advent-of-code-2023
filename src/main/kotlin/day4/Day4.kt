package day4

import helper.Debug
import helper.pow
import kotlin.math.min


fun solveA(text: String, debug: Debug = Debug.Disabled): Int {
    val regex = "Card +\\d+: ([\\d ]+) \\| ([\\d ]+)".toRegex()
    return text.lines().sumOf { line ->
        val (_, winningNumbers, myNumbers) = regex.matchEntire(line)!!.groupValues.map {
            it.split(" ").filter { it.isNotEmpty() }
        }

        val count = myNumbers.count { it in winningNumbers }

        if (count == 0) {
            0
        } else {
            2.pow(count - 1)
        }
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Int {
    val regex = "Card +\\d+: ([\\d ]+) \\| ([\\d ]+)".toRegex()
    val originalCards = text.lines().map { line ->
        val (_, winningNumbers, myNumbers) = regex.matchEntire(line)!!.groupValues.map { matchedGroup ->
            matchedGroup.split(" ").filter { it.isNotEmpty() }
        }

        myNumbers.count { it in winningNumbers }
    }

    val cardCount = originalCards.size
    val cardCounts = Array(cardCount) { 1 }

    for (i in 0 until cardCount - 1) {
        val winCount = originalCards[i]

        if (winCount > 0) {
            for (n in i + 1..min(i + winCount, cardCounts.lastIndex)) {
                cardCounts[n] += cardCounts[i]
            }
        }
    }

    return cardCounts.sum()
}
