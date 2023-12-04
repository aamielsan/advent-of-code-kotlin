import kotlin.math.pow

fun main() {
    val test: List<String> = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent().lines()

    fun part1(input: List<String>): Int =
        input
            .map(ScratchCard::from)
            .sumOf(ScratchCard::points)

    fun part2(input: List<String>): Int {
        val cards = input.map(ScratchCard::from)
        // TODO: not happy with mutability but will live with it for now given work day
        val cardCopies = MutableList(input.size) { 1 }

        cards.forEachIndexed { index, scratchCard ->
            val count = scratchCard.countWinningNumbers()
            val copies = cardCopies[index]
            (index + 1 .. index + count).forEach { cardCopies[it] += copies }
        }

        return cardCopies.sum()
    }

    val input: List<String> = inputAsLines("Day04")

    check(part1(test).println() == 13)
    part1(input).println()
    check(part2(test).println() == 30)
    part2(input).println()
}

data class ScratchCard(
    val winningNumbers: Set<Int>,
    val scratchedNumbers: Set<Int>,
) {
    companion object
}

fun ScratchCard.Companion.from(input: String): ScratchCard =
    input
        .split(": ")
        .last()
        .split(" | ")
        .map { it.split(" ").filter(String::isNotEmpty) }
        .let { (winning, scratched) ->
            ScratchCard(
                winningNumbers = winning.map(String::toInt).toSet(),
                scratchedNumbers = scratched.map(String::toInt).toSet(),
            )
        }

fun ScratchCard.countWinningNumbers() = (scratchedNumbers intersect winningNumbers).size

fun ScratchCard.points(): Int =
    countWinningNumbers().let { if (it == 0) 0 else 2.0.pow(it - 1).toInt() }
