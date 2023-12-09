fun main() {
    val test = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent().lines()

    fun part1(input: List<String>): Int {
        fun predictionFrom(history: List<Int>): Int {
            val pairs = history.zipWithNext()
            return when {
                pairs.all { it.second - it.first == 0 } -> history.first()
                else -> {
                    val prediction = predictionFrom(pairs.map { it.second - it.first })
                    prediction + history.last()
                }
            }
        }

        return input
            .sumOf { line ->
                line
                    .split(" ")
                    .map(String::toInt)
                    .let(::predictionFrom)
            }
    }

    fun part2(input: List<String>): Int {
        fun predictionFrom(history: List<Int>): Int {
            val pairs = history.zipWithNext()
            return when {
                pairs.all { it.second - it.first == 0 } -> history.first()
                else -> {
                    val prediction = predictionFrom(pairs.map { it.second - it.first })
                    history.first() - prediction
                }
            }
        }

        return input
            .sumOf { line ->
                line
                    .split(" ")
                    .map(String::toInt)
                    .let(::predictionFrom)
            }
    }

    val input: List<String> = inputAsLines("Day09")

    check(part1(test) == 114)
    part1(input).println()
    check(part2(test) == 2)
    part2(input).println()
}
