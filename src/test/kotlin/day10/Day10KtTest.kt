package day10

import helper.Debug
import helper.readDayFile
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Disabled
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class Day10KtTest {

    private val day = 10

    @Test
    fun sample1() {
        val text = """
S-7
|.|
L-J
        """.trim()

        assertEquals(4, solveA(text, Debug.Enabled))
        assertEquals(1, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
..F7.
.FJ|.
SJ.L7
|F--J
LJ...""".trim()

        assertEquals(8, solveA(text, Debug.Enabled))
        assertEquals(1, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample5() {
        val text = """
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
        """.trimIndent()


        assertEquals(10, solveB(text, Debug.Disabled))
    }

    @Test
    fun sample3() {
        val text = """
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...""".trim()

        assertEquals(8, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample4() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
...........
.S------7.
.|F----7|.
.||....||.
.||....||.
.|L-7F-J|.
.|..||..|.
.L--JL--J.
...........""".trim()

        assertEquals(4, solveB(text, Debug.Disabled))
    }
    @Test
    fun sample6() {
//        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        val text = """
FS----7F----7
L---7.||.F--J
F---J.LJ.L--7
|.F---7.F---J
|.|...|.|....
|.L-7FJ.|....
L---JL--J....
""".trim()

        assertEquals(10, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(6613, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(511, solveB)
    }
}