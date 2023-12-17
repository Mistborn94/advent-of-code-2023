package day17

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day17KtTest {

    private val day = 17

    @Test
    fun sample1() {
        val text = """
            |2413432311323
            |3215453535623
            |3255245654254
            |3446585845452
            |4546657867536
            |1438598798454
            |4457876987766
            |3637877979653
            |4654967986887
            |4564679986453
            |1224686865563
            |2546548887735
            |4322674655533
        """.trimMargin().trimEnd()

        assertEquals(102, solveA(text, Debug.Enabled))
        assertEquals(94, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1039, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1201, solveB)
    }
}