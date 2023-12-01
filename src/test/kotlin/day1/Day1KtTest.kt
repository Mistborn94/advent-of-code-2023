package day1

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


internal class Day1KtTest {

    private val day = 1

    @Test
    fun sample1() {
        val text = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
        """.trimMargin().trimEnd()

        assertEquals(142, solveA(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
        val text = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen"""

        assertEquals(281, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(54081, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertNotEquals(54491, solveB)
        assertEquals(54649, solveB)
    }
}