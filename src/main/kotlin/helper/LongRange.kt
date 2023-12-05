package helper

import kotlin.math.max
import kotlin.math.min

class CompositeLongRange private constructor(ranges: List<LongRange>) {

    companion object {
        val EMPTY = CompositeLongRange(emptyList())
    }
    private val ranges: List<LongRange> = simplify(ranges)

    private fun simplify(ranges: List<LongRange>): List<LongRange> {
        if (ranges.size <= 1) {
            return ranges
        }

        val newRanges = mutableListOf<LongRange>()
        val rangesToVisit = ranges.sortedBy { it.first }.toMutableList()

        while (rangesToVisit.isNotEmpty()) {
            var next = rangesToVisit.removeFirst()
            if (!next.isEmpty()) {

                while (newRanges.isNotEmpty() && newRanges.last().canMerge(next)) {
                    val previous = newRanges.removeLast()
                    next = previous.merge(next)
                }

                newRanges.add(next)
            }
        }
        return newRanges
    }

    constructor() : this(emptyList())
    constructor(range: LongRange) : this(listOf(range))

    operator fun minus(subtract: LongRange): CompositeLongRange {
        if (subtract.isEmpty()) {
            return this
        }
        val newRanges = ArrayList<LongRange>(ranges.size + 2)
        ranges.forEach {
            val newEnd = subtract.last + 1
            val newStart = subtract.first - 1
            if (it.contains(newStart) || it.contains(newEnd)) {
                if (it.contains(newStart)) {
                    newRanges.add(it.first..newStart)
                }
                if (it.contains(newEnd)) {
                    newRanges.add(newEnd..it.last)
                }
            } else if (!subtract.contains(it.first) || !subtract.contains(it.last)) {
                newRanges.add(it)
            }
        }
        return CompositeLongRange(newRanges)
    }

    operator fun plus(other: CompositeLongRange): CompositeLongRange {
        return CompositeLongRange(ranges + other.ranges)
    }

    operator fun plus(addition: LongRange): CompositeLongRange {
        if (addition.isEmpty()) {
            return this
        }
        if (ranges.isEmpty()) {
            return CompositeLongRange(listOf(addition))
        }

        return CompositeLongRange(ranges + listOf(addition))
    }

    fun overlap(other: LongRange): CompositeLongRange {
        val new = ranges.mapNotNull { component ->
            component.overlap(other).takeIf { overlap -> !overlap.isEmpty() }
        }
        return CompositeLongRange(new)
    }

    fun shift(offset: Long): CompositeLongRange {
        val newRanges = ranges.map { longRange -> (longRange.first + offset)..(longRange.last + offset) }
        return CompositeLongRange(newRanges)
    }

    fun count() = ranges.sumOf { it.last - it.first + 1 }
    fun first() = ranges.first().first

    override fun toString(): String = ranges.joinToString(prefix = "[", postfix = "]", separator = ",")
    fun isEmpty(): Boolean = ranges.isEmpty() || ranges.all { it.isEmpty() }
}

fun LongRange.canMerge(other: LongRange) = this.overlaps(other) || this.adjacentTo(other)

fun LongRange.overlaps(other: LongRange): Boolean {
    return this.first <= other.first && other.first <= this.last
            || other.first < this.first && this.first <= other.last
}

fun LongRange.merge(other: LongRange): LongRange {
    if (!this.canMerge(other)) throw IllegalArgumentException("Cannot merge non-overlapping, non-adjacent ranges")

    return min(this.first, other.first) .. max(this.last, other.last)
}

private fun LongRange.adjacentTo(other: LongRange): Boolean {
    val a = if (this.first < other.first) this else other
    val b = if (this.first < other.first) other else this

    return a.last + 1 == b.first
}

fun LongRange.overlap(other: LongRange): LongRange {
    val a = if (this.first < other.first) this else other
    val b = if (this.first < other.first) other else this

    return if (a.last < b.first) {
        LongRange.EMPTY
    } else {
        max(a.first, b.first)..min(a.last, b.last)
    }
}

