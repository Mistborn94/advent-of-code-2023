package day22

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day22KtTest {

    private val day = 22

    @Test
    fun sample1() {
        val text = """
            |1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9
        """.trimMargin().trimEnd()

        assertEquals(5, solveA(text, Debug.Enabled))
        assertEquals(7, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(413, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(41610, solveB)
    }
}