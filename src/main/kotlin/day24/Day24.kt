package day24

import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import helper.Debug
import helper.pairwise
import helper.point.Point3DLong

fun solveA(
    text: String,
    debug: Debug = Debug.Disabled,
    intersectRange: LongRange = 200000000000000..400000000000000
): Int {

    val hailstones = parseInput(text)

    return hailstones.pairwise(hailstones).count { (a, b) ->
        val intersect = a.xyIntersect(b)

        intersect != null && a.x(intersect.first) in intersectRange && a.y(intersect.first) in intersectRange
    }
}

private operator fun LongRange.contains(double: Double): Boolean = double >= first && double <= endInclusive

private operator fun <E> List<E>.component6(): E = this[5]

data class Hailstone(val p: Point3DLong, val v: Point3DLong) {
    private val slopeXY: Double = v.y.toDouble() / v.x
    private val cXY = -slopeXY * p.x + p.y

    fun tFromX(x: Double) = (x - p.x) / v.x

    fun xyIntersect(other: Hailstone): Pair<Double, Double>? {
        if (this.slopeXY == other.slopeXY) {
            return null
        }
        val x = (this.cXY - other.cXY) / (other.slopeXY - this.slopeXY)
        return (tFromX(x) to other.tFromX(x)).takeIf { (first, second) -> first >= 0 && second >= 0 }
    }

    fun x(t: Double): Double = p.x + t * v.x
    fun y(t: Double): Double = p.y + t * v.y
    fun z(t: Double): Double = p.z + t * v.z

    fun z3(i: Int): List<String> {
        val t = "t$i"
        return listOf(
            "(declare-const $t Int)",
            "(assert (>= $t 0))",
            "(assert (= (+ x (* xv $t)) (+ ${p.x} (* ${v.x} $t))))",
            "(assert (= (+ y (* yv $t)) (+ ${p.y} (* ${v.y} $t))))",
            "(assert (= (+ z (* zv $t)) (+ ${p.z} (* ${v.z} $t))))",
        )
    }

    override fun toString(): String {
        return "$p @ $v -- y = ${slopeXY}x + $cXY"
    }
}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {

    val hailstones = parseInput(text)

    return solveBruteForce(hailstones, debug)
    return solveZ3(hailstones, debug)
}

private fun parseInput(text: String) = text.lines().map {
    val (p1, p2, p3, v1, v2, v3) = it.split(" @ ", ", ")

    Hailstone(
        Point3DLong(p1.trim().toLong(), p2.trim().toLong(), p3.trim().toLong()),
        Point3DLong(v1.trim().toLong(), v2.trim().toLong(), v3.trim().toLong())
    )
}

fun solveBruteForce(hailstones: List<Hailstone>, debug: Debug): Long {
    val relevantStones = hailstones.subList(0, 3)

    for (xv in -500L..500) {
        for (yv in -500L..500) {
            val xyv = Point3DLong(xv, yv, 0)
            val xyRays = relevantStones.map { it.copy(v = it.v - xyv) }
            val intersectAB = xyRays[0].xyIntersect(xyRays[1])
            val intersectAC = xyRays[0].xyIntersect(xyRays[2])

            if (intersectAB != null && intersectAC != null && intersectAB.first == intersectAC.first) {
                val ta = intersectAB.first
                val tb = intersectAB.second
                val tc = intersectAC.second

                for (zv in -500L..500) {
                    val zOffset = Point3DLong(0, 0, zv)
                    val zRays = xyRays.map { it.copy(v = it.v - zOffset) }

                    val zA = zRays[0].z(ta).toLong()
                    val zB = zRays[1].z(tb).toLong()
                    val zC = zRays[2].z(tc).toLong()
                    if (zA == zB && zA == zC) {
                        val x = xyRays[0].x(ta).toLong()
                        val y = xyRays[0].y(ta).toLong()
                        return x + y + zA
                    }
                }
            }
        }
    }

    throw IllegalArgumentException("No solution found :(")
}

private fun solveZ3(hailstones: List<Hailstone>, debug: Debug): Long {
    val start = listOf(
        """(declare-const x Int)""",
        """(declare-const y Int)""",
        """(declare-const z Int)""",
        """(declare-const xv Int)""",
        """(declare-const yv Int)""",
        """(declare-const zv Int)"""
    ).joinToString(separator = "\n")

    val middle = hailstones.subList(0, 3).flatMapIndexed { i, hailstone ->
        hailstone.z3(i)
    }.joinToString(separator = "\n")

    debug {
        println("""Z3 Input:""")
        println(start)
        println(middle)
    }

    val context = Context()
    val boolExprs =
        context.parseSMTLIB2String("$start$middle", emptyArray(), emptyArray(), emptyArray(), emptyArray())
    val solver = context.mkSolver()

    solver.add(*boolExprs)
    solver.check()

    return (solver.model.constDecls.sumOf {
        debug {
            println(" ${it.name} = ${solver.model.getConstInterp(it)}")
        }
        if (it.name.toString() in setOf("x", "y", "z")) {
            (solver.model.getConstInterp(it) as IntNum).getInt64()
        } else {
            0L
        }
    })
}
