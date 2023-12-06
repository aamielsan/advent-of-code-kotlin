import kotlin.math.abs
import kotlin.math.sqrt

fun main() {
    // Seems to be a basic non-linear problem:
    // x^2 - time * x + distance <= 0 ; where x is the hold time
    val test = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent().lines()

    fun part1(input: List<String>) =
        input
            .map { it.split(":").last().split(" ").filter(String::isNotEmpty).map(String::toDouble) }
            .let { (time, distance) -> time.zip(distance) }
            .fold(1) { acc, (time, distance) -> acc * numberOfWaysToWin(time, distance) }

    fun part2(input: List<String>) =
        input
            .map { it.split(":").last().replace(" ", "").toDouble() }
            .let { (time, distance) -> numberOfWaysToWin(time, distance) }

    val input: List<String> = inputAsLines("Day06")

    check(part1(test).println() == 288)
    part1(input).println()
    check(part2(test).println() == 71503)
    part2(input).println()
}

private fun numberOfWaysToWin(time: Double, distance: Double): Int {
    val quadratic = sqrt((time * time) - (4 * distance + 1))
    val firstRoot = ((time + quadratic) / 2)
    val secondRoot = ((time - quadratic) / 2)
    return abs(firstRoot.toInt() - secondRoot.toInt())
}
