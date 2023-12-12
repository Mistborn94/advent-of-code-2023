package day12

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day12KtTest {

    private val day = 12

    @Test
    fun sample1() {
        val text = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
        """.trimMargin().trimEnd()

        assertEquals(21, solveA(text, Debug.Enabled))
        assertEquals(525152, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |?###???????? 3,2,1
        """.trimMargin().trimEnd()

        assertEquals(10, solveA(text, Debug.Enabled))
//        assertEquals(16384, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(7670, solveA)

        val solveB = solveB(lines,Debug.Disabled)
        println("B: $solveB")
        assertEquals(157383940585037, solveB)
    }
}