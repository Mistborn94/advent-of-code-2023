package day8

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day8KtTest {

    private val day = 8

    @Test
    fun sample1() {
        val text = """
            |RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
        """.trimMargin().trimEnd()

        assertEquals(2, solveA(text, Debug.Enabled, "AAA"))

    }

    @Test
    fun sample2() {

        val text = """
            |LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
        """.trimMargin().trimEnd()

        assertEquals(6, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines, startingNode = "AAA")
        println("A: $solveA")
        assertEquals(17263, solveA)

        val solveB = solveB(lines, Debug.Enabled)
        println("B: $solveB")
        assertEquals(14631604759649, solveB)
    }
}