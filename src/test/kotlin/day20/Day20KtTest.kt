package day20

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day20KtTest {

    private val day = 20

    @Test
    fun sample1() {
        val text = """
            |broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
        """.trimMargin().trimEnd()

        assertEquals(32000000, solveA(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
        """.trimMargin().trimEnd()

        assertEquals(11687500, solveA(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(794930686, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(244465191362269, solveB)
    }
}