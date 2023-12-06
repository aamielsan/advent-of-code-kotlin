fun main() {
    val test = """
    """.trimIndent().lines()

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input: List<String> = inputAsLines("Day06")

    check(part1(test).println() == 1)
    part1(input).println()
    check(part2(test).println() == 1)
    part2(input).println()
}
