package day24

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day24KtTest {

    private val day = 24

    @Test
    fun sample1() {
        val text = """
            |19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3
        """.trimMargin().trimEnd()

        assertEquals(2, solveA(text, Debug.Enabled, 7L..27L))
        assertEquals(47, solveB(text, Debug.Enabled))
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
        assertEquals(16172, solveA)

        val solveB = solveB(lines, Debug.Disabled)
        println("B: $solveB")
        assertEquals(600352360036779, solveB)
    }
}