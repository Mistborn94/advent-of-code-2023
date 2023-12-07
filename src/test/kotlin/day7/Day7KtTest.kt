package day7

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day7KtTest {

    private val day = 7

    @Test
    fun sample1() {
        val text = """
            |32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
        """.trimMargin().trimEnd()

        assertEquals(6440, solveA(text, Debug.Enabled))
        assertEquals(5905, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(251806792, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(252113488, solveB)
    }
}