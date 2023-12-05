package day5

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
        CategoryRange(split[0], split[1], split[2])
    }
    CategoryMapping(source, destination, ranges)
}

data class CategoryRange(val destination: Long, val sourceStart: Long, val range: Long) {
    private val sourceRange = sourceStart until (sourceStart + range)

    operator fun contains(source: Long) = sourceRange.contains(source)

    fun map(sourceValue: Long): Long {
        val offset = sourceValue - sourceStart
        return destination + offset
    }
}

data class CategoryMapping(val source: String, val destination: String, val ranges: List<CategoryRange>) {

    fun map(sourceValue: Long): Long {
        val mapping = ranges.find { sourceValue in it }
        return mapping?.map(sourceValue) ?: sourceValue
    }
}


fun solveB(text: String, debug: Debug = Debug.Disabled): Long {
    val chunks = text.split("\n\n")

    val (seedsString) = seedsRegex.matchEntire(chunks[0])!!.destructured
    val seedRanges = seedsString.split(" ").map { it.toLong() }.chunked(2)
        .map { (a, b) -> a until a + b }

    val categories = categoryMappings(chunks)

    return seedRanges.parallelStream().mapToLong { seeds ->
        debug { println("Starting range $seeds") }
        seeds.minOf { seed ->
            debug { println("=============================") }
            categories.fold(seed) { acc, categoryMapping ->
                val map = categoryMapping.map(acc)
                debug { println("Mapped $acc ${categoryMapping.source} to $map ${categoryMapping.destination}") }
                map
            }
        }
    }.min().asLong
}
