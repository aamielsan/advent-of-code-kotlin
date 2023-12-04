fun main() {
    fun part1(input: List<String>): Int {
        val regex = "\\d".toRegex()
        return input.sumOf { line ->
            val first = line.find { regex.matches("$it") }!!
            val last = line.findLast { regex.matches("$it") }!!
            (first.digitToInt() * 10) + (last.digitToInt())
        }
    }

    // map("one" to 1, "1" to 1, ...)
    val digitMap = listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
    ).foldIndexed(emptyMap<String, Int>()) { index, acc, word -> acc + (word to index + 1) + ("${index + 1}" to index + 1) }

    fun String.toDigit(): Int = digitMap.getValue(this)

    fun part2(input: List<String>): Int {
        val digitRegex = "\\d|one|two|three|four|five|six|seven|eight|nine".toRegex()
        return input
            .map { digitRegex.findAll(it) }
            .sumOf { it.first().value.toDigit() * 10 + it.last().value.toDigit() }
    }

    val input = inputAsLines("Day01")

    part1(input).println()
    part2(input).println()
}
