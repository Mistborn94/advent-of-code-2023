package helper.point

data class Point(val x: Int, val y: Int) {

    fun abs(): Int {
        return kotlin.math.abs(x) + kotlin.math.abs(y)
    }

    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun times(value: Int) = Point(x * value, y * value)

    /**
     * Rotate the point clockwise around the origin
     */
    fun clockwise(degrees: Int): Point {
        return when (degrees) {
            90 -> Point(y, -x)
            180 -> Point(-x, -y)
            270 -> Point(-y, x)
            else -> throw UnsupportedOperationException()
        }
    }

    /**
     * Rotate the point counterclockwise around the origin
     */
    fun counterClockwise(degrees: Int): Point = clockwise(360 - degrees)

    fun neighbours() = listOf(
        Point(x + 1, y),
        Point(x - 1, y),
        Point(x, y + 1),
        Point(x, y - 1)
    )

    fun diagonalNeighbours() = listOf(
        Point(x + 1, y + 1),
        Point(x + 1, y - 1),
        Point(x - 1, y + 1),
        Point(x - 1, y - 1)
    )

    companion object {
        val ZERO = Point(0, 0)
    }
}

data class LongPoint(val x: Long, val y: Long) {

    fun abs(): Long {
        return kotlin.math.abs(x) + kotlin.math.abs(y)
    }

    operator fun minus(other: LongPoint): LongPoint = LongPoint(x - other.x, y - other.y)
    operator fun plus(other: LongPoint) = LongPoint(x + other.x, y + other.y)
    operator fun times(value: Long) = LongPoint(x * value, y * value)

    fun neighbours() = listOf(
        LongPoint(x + 1, y),
        LongPoint(x - 1, y),
        LongPoint(x, y + 1),
        LongPoint(x, y - 1)
    )

    fun diagonalNeighbours() = listOf(
        LongPoint(x + 1, y + 1),
        LongPoint(x + 1, y - 1),
        LongPoint(x - 1, y + 1),
        LongPoint(x - 1, y - 1)
    )

    /**
     * Rotate the point clockwise around the origin
     */
    fun clockwise(degrees: Int): LongPoint {
        return when (degrees) {
            90 -> LongPoint(y, -x)
            180 -> LongPoint(-x, -y)
            270 -> LongPoint(-y, x)
            else -> throw UnsupportedOperationException()
        }
    }

    /**
     * Rotate the point counterclockwise around the origin
     */
    fun counterClockwise(degrees: Int): LongPoint = clockwise(360 - degrees)

    companion object {
        val ZERO = LongPoint(0, 0)
    }
}

@JvmName("strGet")
operator fun List<String>.get(point: Point): Char = this[point.y][point.x]
@JvmName("strContains")
operator fun Collection<String>.contains(point: Point): Boolean =
    this.isNotEmpty() && point.y in this.indices && point.x in this.first().indices
@JvmName("strPoints")
fun List<String>.points(): List<Point> = indices.flatMap { y -> this[y].indices.map { x -> Point(x, y) } }
@JvmName("strFindAll")
fun List<String>.findAll(char: Char): List<Point> = flatMapIndexed { y: Int, row: String ->
    row.mapIndexedNotNull { x, c -> if (c == char) Point(x, y) else null }
}


operator fun <E> List<List<E>>.get(point: Point) = this[point.y][point.x]
operator fun <E> List<MutableList<E>>.set(point: Point, value: E) {
    this[point.y][point.x] = value
}
operator fun <E> Collection<Collection<E>>.contains(point: Point): Boolean =
    this.isNotEmpty() && point.y in this.indices && point.x in this.first().indices

fun <E> List<Collection<E>>.points(): List<Point> = indices.flatMap { y ->
    this[y].indices.map { x -> Point(x, y) }
}

fun <E> List<Collection<E>>.indexOf(item: E): Point =
    this.mapIndexedNotNull { y, line -> line.indexOf(item).let { x -> if (x != -1) Point(x, y) else null } }.first()
fun <E> List<Collection<E>>.findAll(item: E): List<Point> = flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, c -> if (c == item) Point(x, y) else null }
}

operator fun <E> Array<Array<E>>.get(point: Point) = this[point.y][point.x]
operator fun <E> Array<Array<E>>.set(point: Point, value: E) {
    this[point.y][point.x] = value
}
operator fun <E> Array<Array<E>>.contains(point: Point): Boolean = this.isNotEmpty() && point.y in this.indices && point.x in this.first().indices
fun <E> Array<Array<E>>.indexOf(item: E): Point =
    this.mapIndexedNotNull { y, line -> line.indexOf(item).let { x -> if (x != -1) Point(x, y) else null } }.first()
fun <E> Array<Array<E>>.findAll(item: E): List<Point> = flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, c -> if (c == item) Point(x, y) else null }
}
