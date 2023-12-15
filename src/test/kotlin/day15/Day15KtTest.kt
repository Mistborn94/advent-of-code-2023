package day15

import helper.Debug
import helper.readDayFile
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day15KtTest {

    private val day = 15

    @Test
    fun sample1() {
        val text = """
            |HASH
        """.trimMargin().trimEnd()

        assertEquals(52, solveA(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
        val text = """
            |rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """.trimMargin().trimEnd()

        assertEquals(1320, solveA(text, Debug.Enabled))
        assertEquals(145, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(497373, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(259356, solveB)
    }
}