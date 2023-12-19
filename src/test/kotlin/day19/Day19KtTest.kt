package day19

import helper.Debug
import helper.readDayFile
import org.junit.jupiter.api.Assertions.assertNotEquals
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class Day19KtTest {

    private val day = 19

    @Test
    fun sample1() {
        val text = """
            |px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
        """.trimMargin().trimEnd()

        assertEquals(19114, solveA(text, Debug.Enabled))
        assertEquals(167409079868000, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(421983, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(129249871135292, solveB)
    }
}