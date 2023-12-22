package helper.point

import helper.overlaps
import helper.point.base.Rectangle

data class Cube(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange, val label: String = "") {

    fun grow(size: Int = 1): Cube {
        return Cube(
            xRange.first - size..xRange.last + size,
            yRange.first - size..yRange.last + size,
            zRange.first - size..zRange.last + size
        )
    }

    fun translate(offset: Point3D): Cube {
        return Cube(translateRange(xRange, offset.x), translateRange(yRange, offset.y), translateRange(zRange, offset.z), label)
    }

    private fun translateRange(range: IntRange, offset: Int): IntRange {
        return if (offset == 0) {
            range
        } else {
            (range.first + offset)..(range.last + offset)
        }
    }

    operator fun contains(point: Point3D) = point.x in xRange && point.y in yRange && point.z in zRange

    fun overlaps(other: Cube): Boolean {
        return xRange.overlaps(other.xRange) && yRange.overlaps(other.yRange) && zRange.overlaps(other.zRange)
    }

    fun xyView(): Rectangle = Rectangle(xRange, yRange, label)
    fun xzView(): Rectangle = Rectangle(xRange, zRange, label)
    fun yzView(): Rectangle = Rectangle(yRange, zRange, label)

    companion object {
        fun boundingBoxOf(a: Point3D, b: Point3D, label: String = ""): Cube {
            val minY = minOf(a.y, b.y)
            val minX = minOf(a.x, b.x)
            val minZ = minOf(a.z, b.z)

            val maxY = maxOf(a.y, b.y)
            val maxX = maxOf(a.x, b.x)
            val maxZ = maxOf(a.z, b.z)

            return Cube(minX..maxX, minY..maxY, minZ..maxZ, label)
        }

        fun boundingBoxOf(points: MutableSet<Point3D>): Cube {
            var minX = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE

            var minY = Int.MAX_VALUE
            var maxY = Int.MIN_VALUE

            var minZ = Int.MAX_VALUE
            var maxZ = Int.MIN_VALUE

            points.forEach {
                minX = minOf(minX, it.x)
                maxX = maxOf(maxX, it.x)

                minY = minOf(minY, it.y)
                maxY = maxOf(maxY, it.y)

                minZ = minOf(minZ, it.z)
                maxZ = maxOf(maxZ, it.z)
            }

            return Cube(minX..maxX, minY..maxY, minZ..maxZ)
        }
    }
}