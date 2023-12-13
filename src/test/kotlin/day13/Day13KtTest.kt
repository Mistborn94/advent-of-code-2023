package day13

import helper.Debug
import helper.readDayFile
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day13KtTest {

    private val day = 13

    @Test
    fun sample1() {
        val text = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
        """.trimMargin().trimEnd()

        assertEquals(405, solveA(text, Debug.Enabled))
        assertEquals(400, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample1B() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
        """.trimMargin().trimEnd()

        assertEquals(400, solveA(text, Debug.Enabled))
        assertEquals(100, solveB(text, Debug.Enabled))
    }
    @Test
    fun sample1A() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.""".trimMargin().trimEnd()

        assertEquals(5, solveA(text, Debug.Enabled))
        assertEquals(300, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |######....#######
..#...####...#..#
.###.#....#.###.#
....##.##.##....#
.##....##....##..
.####..##..####.#
##..#..##..#..###
..#.########.#...
#..#.######.#..#.
.##..#.##.#..##..
###.#.#..#.#.###.
###.#.####.#.###.
.##..#.##.#..##..
#..#.######.#..#.
..#.########.#...
""".trimMargin().trimEnd()

        assertEquals(8, solveA(text, Debug.Enabled))
//        assertEquals(300, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample3() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
            |######....#######
..#...####...#..#
.###.#....#.###.#
....##.##.##....#
.##....##....##..
.####...#..####.#
##..#..##..#..###
..#.########.#...
#..#.######.#..#.
.##..#.##.#..##..
###.#.####.#.###.
###.#.####.#.###.
.##..#.##.#..##..
#..#.######.#..#.
..#.########.#...
""".trimMargin().trimEnd()

        assertEquals(8, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertNotEquals(33038, solveA)
        assertEquals(33047, solveA)

        val solveB = solveB(lines, Debug.Enabled)
        println("B: $solveB")
        assertNotEquals(32334, solveB)
//        assertEquals(0, solveB)
    }
}