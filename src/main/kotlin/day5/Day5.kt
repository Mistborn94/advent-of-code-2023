package day5

import helper.CompositeLongRange
import helper.Debug

val seedsRegex = "seeds: ([\\d ]+)".toRegex()
val mapTitle = "([a-z]+)-to-([a-z]+) map:".toRegex()

fun solveA(text: String, debug: Debug = Debug.Disabled): Long {
    val chunks = text.split("\n\n")

    val (seedsString) = seedsRegex.matchEntire(chunks[0])!!.destructured
    val seeds = seedsString.split(" ").map { it.toLong() }

    val categories = categoryMappings(chunks)

    return seeds.minOf { seed ->
        debug { println("=============================") }
        categories.fold(seed) { acc, categoryMapping ->
            val map = categoryMapping.map(acc)
            debug { println("Mapped $acc ${categoryMapping.source} to $map ${categoryMapping.destination}") }
            map
        }
    }
}

private fun categoryMappings(chunks: List<String>) = chunks.drop(1).map { chunk ->
    val lines = chunk.trim().lines()
    val (source, destination) = mapTitle.matchEntire(lines[0])!!.destructured
    val ranges = lines.drop(1).map { line ->
        val split = line.split(" ").map { it.toLong() }
        CategoryRange(split[1], split[0], split[2])
    }.sorted()
    CategoryMapping(source, destination, ranges)
}

val rangeComparator: Comparator<CategoryRange> =
    compareBy<CategoryRange> { it.source }.thenComparing<Long> { it.destination }.thenComparing<Long> { it.range }

data class CategoryRange(val source: Long, val destination: Long, val range: Long) : Comparable<CategoryRange> {
    private val sourceRange = source until (source + range)
    private val offset = destination - source

    operator fun contains(source: Long) = sourceRange.contains(source)

    fun map(sourceValue: Long): Long {
        val offset = sourceValue - source
        return destination + offset
    }

    override fun compareTo(other: CategoryRange): Int = rangeComparator.compare(this, other)

    fun mapRange(startRange: CompositeLongRange): Pair<CompositeLongRange,CompositeLongRange> {
        val overlap = startRange.overlap(sourceRange)

        return if (overlap.isEmpty()) {
            startRange to CompositeLongRange.emptyLongRange
        } else {
            val remainder = startRange.minus(sourceRange)
            val mapped = overlap.shift(offset)
            if (remainder.isEmpty()) {
                CompositeLongRange.emptyLongRange to mapped
            } else {
                remainder to mapped
            }
        }

    }
}

data class CategoryMapping(val source: String, val destination: String, val ranges: List<CategoryRange>) {

    fun map(sourceValue: Long): Long {
        val mapping = ranges.find { sourceValue in it }
        return mapping?.map(sourceValue) ?: sourceValue
    }

    fun mapRange(startRange: CompositeLongRange): CompositeLongRange {
        val (unmapped, mapped) = ranges.fold(startRange to CompositeLongRange.emptyLongRange) { (unmappedAcc, mappedAcc), categoryMapping ->
            val (unmappedNew, mappedNew) = categoryMapping.mapRange(unmappedAcc)
            unmappedNew to (mappedAcc + mappedNew)
        }
        return unmapped + mapped
    }


}


fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val chunks = text.split("\n\n")

    val (seedsString) = seedsRegex.matchEntire(chunks[0])!!.destructured
    val seedRanges = seedsString.split(" ").map { it.toLong() }.chunked(2)
        .map { (a, b) -> a until a + b }

    val categories = categoryMappings(chunks)
    debug { println("Categories are ${categories.joinToString(separator = "\n")}") }

    return seedRanges.minOf { seeds ->
        debug { println("=====================================") }
        debug { println("Starting range $seeds") }
        val first = categories.fold(CompositeLongRange.newLongRange(seeds)) { acc, categoryMapping ->
            val mappedRange = categoryMapping.mapRange(acc)
            debug { println("Mapped  ${categoryMapping.source} to ${categoryMapping.destination}: $acc => $mappedRange") }
            mappedRange
        }.first()
        debug { println("First is $first") }
        first
    }
}
