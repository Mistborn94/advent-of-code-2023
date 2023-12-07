package day7

import helper.Debug
import kotlin.math.min

val fiveOfAKind = { hand: Map<Char, Int> -> hand.size == 1 }
val fourOfAKind = { hand: Map<Char, Int> -> hand.size == 2 && hand.values.max() == 4 }
val fullHouse = { hand: Map<Char, Int> -> hand.size == 2 && hand.values.max() == 3 && hand.values.min() == 2 }
val threeOfAKind = { hand: Map<Char, Int> -> hand.size == 3 && hand.values.max() == 3 }
val twoPair = { hand: Map<Char, Int> -> hand.size == 3 && hand.values.max() == 2 }
val pair = { hand: Map<Char, Int> -> hand.size == 4 && hand.values.max() == 2 }
val highCard = { hand: Map<Char, Int> -> hand.size == 5 }

val handTypes = listOf(fiveOfAKind, fourOfAKind, fullHouse, threeOfAKind, twoPair, pair, highCard)

val cardValuesA = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
val cardValuesB = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

fun solveA(text: String, debug: Debug = Debug.Disabled): Int = solve(text, false)
fun solveB(text: String, debug: Debug = Debug.Disabled): Int = solve(text, true)

private fun solve(text: String, part2: Boolean): Int {
    val hands = text.lines().map { line ->
        val (cards, bid) = line.split(' ')

        Hand(cards, bid.toInt(), part2)
    }.sorted()

    return hands.mapIndexed { index, hand ->
        val rank = hands.size - index
        rank * hand.bid
    }.sum()
}

class Hand(private val cards: String, val bid: Int, private val part2: Boolean) : Comparable<Hand> {
    private val cardCounts = cards.groupBy { it }.mapValues { (_, v) -> v.size }
    private val typeA = handTypes.indexOfFirst { it(cardCounts) }
    private val typeB = bestTypeWithJoker()

    private val type: Int
        get() = if (part2) typeB else typeA

    private val cardValues = if (part2) cardValuesB else  cardValuesA
    private val cardRanks by lazy { cards.map {cardValues.indexOf(it) } }

    private fun bestTypeWithJoker(): Int {
        if (!cards.contains('J')) {
            return typeA
        }
        val typeB = cardValuesB.subList(0, cardValuesB.lastIndex).minOf {
            val replaceFirst = cards.replaceFirst('J', it)
            val newHand = Hand(replaceFirst, bid, replaceFirst.contains('J'))
            newHand.type
        }
        return min(typeA, typeB)
    }

    //Returns zero if this object is equal to the specified other object, a negative number if it's less than other, or a positive number if it's greater than other.
    override fun compareTo(other: Hand): Int {
        val typeCompare = this.type.compareTo(other.type)
        return if (this.cards == other.cards) {
            0
        } else if (typeCompare != 0) {
            typeCompare
        } else {
            val (a, b) = cardRanks.zip(other.cardRanks).first { (a, b) -> a != b }
            a.compareTo(b)
        }
    }

    override fun toString(): String = cards
}
