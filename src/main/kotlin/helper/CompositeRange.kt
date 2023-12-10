package helper

class CompositeRange<T : Comparable<T>>(
    ranges: List<ClosedRange<T>> = emptyList(),
    private val adder: (T, Long) -> T,
    private val factory: (T, T) -> ClosedRange<T> = { a, b -> a..b },
    private val size: (ClosedRange<T>) -> Long
) {

    private val ranges: List<ClosedRange<T>> = simplify(ranges)

    private operator fun T.plus(steps: Long) = adder(this, steps)
    private operator fun T.minus(steps: Long) = adder(this, -steps)
    private fun compositeRange(ranges: List<ClosedRange<T>>): CompositeRange<T> = CompositeRange(ranges, adder, factory, size)

    private fun simplify(ranges: List<ClosedRange<T>>): List<ClosedRange<T>> {
        if (ranges.size <= 1) {
            return ranges
        }

        val newRanges = mutableListOf<ClosedRange<T>>()
        val rangesToVisit = ranges.sortedBy { it.start }.toMutableList()

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

    operator fun minus(subtract: ClosedRange<T>): CompositeRange<T> {
        if (subtract.isEmpty() || this.isEmpty()) {
            return this
        }
        val newRanges = ArrayList<ClosedRange<T>>(ranges.size + 2)
        ranges.forEach {
            val newEnd = subtract.endInclusive + 1
            val newStart = subtract.start - 1
            if (it.contains(newStart) || it.contains(newEnd)) {
                if (it.contains(newStart)) {
                    newRanges.add(it.start..newStart)
                }
                if (it.contains(newEnd)) {
                    newRanges.add(newEnd..it.endInclusive)
                }
            } else if (!subtract.contains(it.start) || !subtract.contains(it.endInclusive)) {
                newRanges.add(it)
            }
        }
        return compositeRange(newRanges)
    }

    operator fun plus(other: CompositeRange<T>): CompositeRange<T> {
        return compositeRange(ranges + other.ranges)
    }

    operator fun plus(addition: ClosedRange<T>): CompositeRange<T> {
        if (addition.isEmpty()) {
            return this
        }
        if (this.isEmpty()) {
            return compositeRange(listOf(addition))
        }

        return compositeRange(ranges + listOf(addition))
    }

    fun overlap(other: ClosedRange<T>): CompositeRange<T> {
        return if (this.isEmpty()) {
            this
        } else {
            val new = ranges.mapNotNull { component ->
                component.overlap(other)?.takeIf { overlap -> !overlap.isEmpty() }
            }
            compositeRange(new)
        }
    }

    fun shift(offset: Long): CompositeRange<T> {
        return if (this.isEmpty()) {
            this
        } else {
            val newRanges =
                ranges.map { longRange -> factory(longRange.start + offset, (longRange.endInclusive + offset)) }
            compositeRange(newRanges)
        }
    }

    fun first() = ranges.first().start
    fun isEmpty(): Boolean = ranges.isEmpty() || ranges.all { it.isEmpty() }
    fun size() = ranges.sumOf { size(it) }

    override fun toString(): String = ranges.joinToString(prefix = "[", postfix = "]", separator = ",")

    private fun ClosedRange<T>.canMerge(other: ClosedRange<T>): Boolean {
        return this.overlaps(other) || this.adjacentTo(other)
    }

    private fun ClosedRange<T>.overlaps(other: ClosedRange<T>): Boolean {
        return this.start <= other.start && other.start <= this.endInclusive
                || other.start < this.start && this.start <= other.endInclusive
    }

    private fun ClosedRange<T>.adjacentTo(other: ClosedRange<T>): Boolean {
        val a = if (this.start < other.start) this else other
        val b = if (this.start < other.start) other else this

        return a.endInclusive + 1 == b.start
    }

    private fun ClosedRange<T>.merge(other: ClosedRange<T>): ClosedRange<T> {
        if (!this.canMerge(other)) throw IllegalArgumentException("Cannot merge non-overlapping, non-adjacent ranges")

        return factory(minOf(this.start, other.start), maxOf(this.endInclusive, other.endInclusive))
    }

    private fun ClosedRange<T>.overlap(other: ClosedRange<T>): ClosedRange<T>? {
        val a = if (this.start < other.start) this else other
        val b = if (this.start < other.start) other else this

        return if (a.endInclusive < b.start) {
            null
        } else {
            factory(maxOf(a.start, b.start), minOf(a.endInclusive, b.endInclusive))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompositeRange<*>

        return ranges == other.ranges
    }

    override fun hashCode(): Int {
        return ranges.hashCode()
    }

    companion object CompositeLongRange {
        private val longRangeFactory: (Long, Long) -> LongRange = { a, b -> a..b }
        private val longAdder: (Long, Long) -> Long = { a, b -> a + b }
        private val longSize: (ClosedRange<Long>) -> Long = { it.endInclusive - it.start + 1 }

        private val intRangeFactory: (Int, Int) -> IntRange = { a, b -> a..b }
        private val intAdder: (Int, Long) -> Int = { a, b -> a + b.toInt() }
        private val intSize: (ClosedRange<Int>) -> Long = { it.endInclusive - it.start + 1L }

        val emptyLongRange: CompositeRange<Long> by lazy { CompositeRange(emptyList(), longAdder, longRangeFactory, longSize) }
        fun newLongRange(range: LongRange) = CompositeRange(listOf(range), longAdder, longRangeFactory, longSize)
        fun newLongRange(ranges: List<LongRange>) = CompositeRange(ranges, longAdder, longRangeFactory, longSize)

        val emptyIntRange: CompositeRange<Int> = CompositeRange(emptyList(), intAdder, intRangeFactory, intSize)
        fun newIntRange(range: IntRange) = CompositeRange(listOf(range), intAdder, intRangeFactory, intSize)
        fun newIntRange(ranges: List<IntRange>) = CompositeRange(ranges, intAdder, intRangeFactory, intSize)
    }
}

typealias CompositeLongRange = CompositeRange<Long>


