package day14

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day14KtTest {

    private val day = 14

    @Test
    fun sample1() {
        val text = """
            |O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
        """.trimMargin().trimEnd()

        assertEquals(136, solveA(text, Debug.Enabled))
        assertEquals(64, solveB(text, Debug.Enabled))
    }

    @Test
    @Ignore
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |
        """.trimMargin().trimEnd()

        assertEquals(0, solveA(text, Debug.Enabled))
        assertEquals(0, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(109596, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(96105, solveB)
    }
}