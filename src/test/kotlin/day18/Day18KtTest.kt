package day18

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day18KtTest {

    private val day = 18

    @Test
    fun sample1() {
        val text = """
            |R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
        """.trimMargin().trimEnd()

        assertEquals(62, solveA(text, Debug.Enabled))
        assertEquals(952408144115, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(61865, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(40343619199142, solveB)
    }
}