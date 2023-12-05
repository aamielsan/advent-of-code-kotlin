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
        val raw = input.split("\n\n").map(String::lines)
        val seeds: List<Long> = raw[0][0].split(": ").last().split(" ").map(String::toLong)
        val transformers = raw.drop(1).map(Mapping::of)

        return seeds
            .map { seed ->
                transformers.fold(seed) { acc, mapping ->
                    val values = mapping.mapNotNull { it.value(acc) }
                    when {
                        values.isEmpty() -> acc
                        else -> values.first() // there doesn't seem to be overlapping ranges
                    }
                }
            }
            .min()
    }

    fun part2(input: String): Long {
        val raw = input.split("\n\n").map(String::lines)
        val seedRanges: List<List<Long>> = raw[0][0].split(": ").last().split(" ").map(String::toLong).chunked(2)
        val transformers = raw.drop(1).map(Mapping::of)

        val locations = seedRanges
            .flatMap { (seed, range) -> List(range.toInt()) { seed + it.toLong() } }
            .map { seed ->
                transformers.fold(seed) { acc, mapping ->
                    val values = mapping.mapNotNull { it.value(acc) }
                    when {
                        values.isEmpty() -> acc
                        else -> values.first() // there doesn't seem to be overlapping ranges
                    }
                }
            }

        return locations.min()
    }

    val input: String = inputAsText("Day05")

    check(part1(test).println() == 35.toLong())
    check(part1(input).println() == 993500720.toLong())
    check(part2(test).println() == 47.toLong())
    part2(input).println()
}

private data class Mapping(
    private val source: Long,
    private val destination: Long,
    private val range: Long,
) {
    companion object {
        fun of(rawMapping: List<String>): List<Mapping> =
            rawMapping
                .drop(1)
                .map { line ->
                    line
                        .split(" ")
                        .map(String::toLong)
                        .let { (dest, source, range) -> Mapping(source, dest, range) }
                }
    }

    fun value(key: Long): Long? =
        when (key) {
            in source..source + range -> destination + (key - source)
            else -> null
        }
}

