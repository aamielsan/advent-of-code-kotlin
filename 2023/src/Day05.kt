import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.time.measureTime

fun main() {
    val test: String = """
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
    """.trimIndent()

    fun part1(input: String): Long {
        val seeds: List<Long> = seed(input)
        val almanac = Almanac(
            mappings = mappings(input)
        )
        return seeds.minOf { almanac locationOf it }
    }

    fun part2(input: String): Long = runBlocking {
        val almanac = Almanac(
            mappings = mappings(input)
        )

        seed(input)
            .chunked(2)
            .sortedBy { it.first() }
            .map { (seed, range) -> seed until seed + range }
            .map { seedRange ->
                async {
                    seedRange.minOf { seed -> almanac locationOf seed }
                }
            }
            .awaitAll()
            .min()
    }

    val input: String = inputAsText("Day05")

    check(part1(test).println() == 35.toLong())
    part1(input).println()
    measureTime {
        check(part2(test).println() == 46.toLong())
    }.println()
    measureTime {
        part2(input).println()
    }.println()
}

private fun seed(input: String): List<Long> =
    input
        .split("\n\n")
        .first()
        .split(": ")
        .last()
        .split(" ")
        .map(String::toLong)

private fun mappings(input: String): List<List<Almanac.Mapping>> =
    input
        .split("\n\n")
        .drop(1)
        .map(String::lines)
        .map { rawMappings ->
            rawMappings
                .drop(1)
                .map { line ->
                    line
                        .split(" ")
                        .map(String::toLong)
                        .let { (dest, source, range) -> Almanac.Mapping(source, dest, range) }
                }
        }

private class Almanac(
    private val mappings: List<List<Mapping>>
) {
    infix fun locationOf(seed: Long): Long =
        mappings.fold(seed) { acc, mapping ->
            mapping.firstNotNullOfOrNull { it.valueOf(acc) } ?: acc
        }

    data class Mapping(
        private val source: Long,
        private val destination: Long,
        private val range: Long,
    ) {
        fun valueOf(key: Long): Long? =
            when (key) {
                in source .. source + range -> destination + (key - source)
                else -> null
            }
    }
}
