package day5

import helper.Debug
import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


internal class Day5KtTest {

    private val day = 5

    @Test
    fun sample1() {
        val text = """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4

        """.trimMargin().trimEnd()

        assertEquals(35, solveA(text, Debug.Disabled))
        assertEquals(46, solveB(text, Debug.Disabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(214922730, solveA)

        val solveB = solveB(lines, Debug.Disabled)
        println("B: $solveB")
        assertEquals(148041808, solveB)
    }
}