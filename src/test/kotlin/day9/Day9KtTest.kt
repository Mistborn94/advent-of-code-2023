package day9

import helper.Debug
import helper.readDayFile
import org.junit.jupiter.api.Order
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day9KtTest {

    private val day = 9

    @Test
    fun sample1() {
        val text = """
            |0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
        """.trimMargin().trimEnd()

        assertEquals(114, solveA(text, Debug.Enabled))
        assertEquals(2, solveB(text, Debug.Enabled))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1992273652, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1012, solveB)
    }
}