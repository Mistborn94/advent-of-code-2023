package day2

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


internal class Day2KtTest {

    private val day = 2

    @Test
    fun sample1() {
        val text = """
            |Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimMargin().trimEnd()

        assertEquals(8, solveA(text, Debug.Enabled))
        assertEquals(2286, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1853, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(72706, solveB)
    }
}