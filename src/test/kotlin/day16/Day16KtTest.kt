package day16

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day16KtTest {

    private val day = 16

    @Test
    fun sample1() {
        val text = """
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
        """.trim()

        assertEquals(46, solveA(text, Debug.Enabled))
        assertEquals(51, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(7046, solveA)

        val solveB = solveB(lines, Debug.Disabled)
        println("B: $solveB")
        assertEquals(7313, solveB)
    }
}