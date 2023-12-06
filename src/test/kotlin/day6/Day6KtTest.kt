package day6

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day6KtTest {

    private val day = 6

    @Test
    fun sample1() {
        val text = """
            |Time:      7  15   30
Distance:  9  40  200
        """.trimMargin().trimEnd()

        assertEquals(288, solveA(text, Debug.Enabled))
        assertEquals(71503, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(74698, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(27563421, solveB)
    }
}