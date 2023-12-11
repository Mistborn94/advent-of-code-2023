package day11

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day11KtTest {

    private val day = 11

    @Test
    fun sample1() {
        val text = """
            |...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
        """.trimMargin().trimEnd()

        assertEquals(374, solve(text, Debug.Enabled))
        assertEquals(1030, solve(text, Debug.Enabled, 10))
        assertEquals(8410, solve(text, Debug.Disabled, 100))
        assertEquals(82000210, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solve(lines)
        println("A: $solveA")
        assertEquals(9639160, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(752936133304, solveB)
    }
}