package day21

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day21KtTest {

    private val day = 21

    @Test
    fun sample1() {
        val text = """
            |...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
        """.trimMargin().trimEnd()

        assertEquals(16, solveA(text, Debug.Disabled, 6))
        assertEquals(33, solveA(text, Debug.Disabled, 10))
        assertEquals(42, solveA(text, Debug.Disabled, 100))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(3847, solveA)

        val solveB = solveB(lines, steps = 26_501_365)
        println("B: $solveB")
        assertEquals(637537341306357, solveB)
    }
}