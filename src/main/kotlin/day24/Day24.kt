package day24

import helper.Debug
import helper.pairwise
import helper.point.Point3DLong

fun solveA(
    text: String,
    debug: Debug = Debug.Disabled,
    intersectRange: LongRange = 200000000000000..400000000000000
): Int {

    val hailstones = text.lines().map {
        val (p1, p2, p3, v1, v2, v3) = it.split(" @ ", ", ")

        HailstoneFormula(
            Point3DLong(p1.trim().toLong(), p2.trim().toLong(), p3.trim().toLong()),
            Point3DLong(v1.trim().toLong(), v2.trim().toLong(), v3.trim().toLong())
        )
    }

    return hailstones.pairwise(hailstones).count { (a, b) ->
        val intersect = a.xyIntersect(b)

        intersect != null && intersect.first in intersectRange && intersect.second in intersectRange
                && a.tFromX(intersect.first) > 0 && b.tFromX(intersect.first) > 0
    }
}

private operator fun LongRange.contains(double: Double): Boolean = double >= first && double <= endInclusive

private operator fun <E> List<E>.component6(): E = this[5]

data class HailstoneFormula(val p: Point3DLong, val v: Point3DLong) {
    val p0 = Point3DLong(x(0L), y(0L), z(0L))
    val p1 = Point3DLong(x(1L), y(1L), z(1L))
    val slopeXY: Double = (p1.y - p0.y).toDouble() / (p1.x - p0.x)

    val cXY = -slopeXY * p0.x + p0.y

    fun yFromX(x: Double) = slopeXY * x + cXY
    fun tFromX(x: Double) = (x - p0.x) / v.x

    fun xyIntersect(other: HailstoneFormula): Pair<Double, Double>? {
        if (this.slopeXY == other.slopeXY) {
            return null
        }
        val x = (this.cXY - other.cXY) / (other.slopeXY - this.slopeXY)
        val y = yFromX(x)
        return x to y
    }

    fun x(t: Long): Long = p.x + t * v.x
    fun y(t: Long): Long = p.y + t * v.y
    fun z(t: Long): Long = p.z + t * v.z

    fun formulas(i: Int): List<String> {
        return listOf(
            "x + vx * t_{$i} = ${p.x} + ${v.x} * t_{$i}",
            "y + vy * t_{$i} = ${p.y} + ${v.y} * t_{$i}",
            "z + vz * t_{$i} = ${p.z} + ${v.z} * t_{$i}"
        )
    }

    override fun toString(): String {
        return "$p @ $v -- y = ${slopeXY}x + $cXY"
    }


}

fun solveB(text: String, debug: Debug = Debug.Disabled): Long {

    val unknownFormulas = listOf(
        "x + vx * t",
        "y + vy * t",
        "z + vz * t"
    )

    val hailstones = text.lines().map {
        val (p1, p2, p3, v1, v2, v3) = it.split(" @ ", ", ")

        HailstoneFormula(
            Point3DLong(p1.trim().toLong(), p2.trim().toLong(), p3.trim().toLong()),
            Point3DLong(v1.trim().toLong(), v2.trim().toLong(), v3.trim().toLong())
        )
    }

    println("t >= 0")
    hailstones.forEachIndexed { i, it ->
        val forumulas = it.formulas(i)

        println(forumulas.joinToString(separator = "\n"))
    }

    return 0L
}
