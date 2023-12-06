import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    // Seems to be a basic non-linear problem:
    // x^2 - time * x + distance <= 0 ; where x is the hold time
    val test: String = """
        Time:      7  15
        Distance:  9  40
    """.trimIndent()

    fun numberOfWaysToWin(time: Double, distance: Double): Double {
        val quadratic = sqrt(time.pow(2.0) - (4 * distance))
        val firstRoot = ceil((time + quadratic) / 2)
        val secondRoot = ceil((time - quadratic) / 2)

        return abs(firstRoot - secondRoot)
    }

    fun part1(input: String): Double =
        input
            .split("\n")
            .map { it.split(":").last().split(" ").filter(String::isNotEmpty).map(String::toDouble) }
            .let { (time, distance) -> time.zip(distance) }
            .fold(1.0) { acc, (time, distance) -> acc * numberOfWaysToWin(time, distance) }

    fun part2(input: String): Double = numberOfWaysToWin(47847467.0, 207139412091014.0)

    val input: String = inputAsText("Day06")

//    check(part1(test).println() == 32.0)
    part1(input).println()
//    check(part2(test).println() == 47.toLong())
    part2(input).toBigDecimal().toPlainString().println()
}
