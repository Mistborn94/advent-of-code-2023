package day3

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day3KtTest {

    private val day = 3

    @Test
    fun sample1() {
        val text = """
            |467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...${'$'}.*....
.664.598..
        """.trimMargin().trimEnd()

        assertEquals(4361, solveA(text, Debug.Disabled))
        assertEquals(467835, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample2() {
        val text = """
            |123+
            |$456
        """.trimMargin().trimEnd()

        assertEquals(579, solveA(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(531561, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(83279367, solveB)
    }
}