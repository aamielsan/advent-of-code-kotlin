fun main() {
    val test: List<String> = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent().lines()

    fun part1(input: List<String>): Int =
        input.sumOf { line ->
            Game.from(line)
                .takeIf(Game::isPossible)
                ?.id ?: 0
        }

    fun part2(input: List<String>): Int = input.sumOf { Game.from(it).power }

    val input: List<String> = inputAsLines("Day02")

    check(part1(test) == 8)
    part1(input).println()

    check(part2(test) == 2286)
    part2(input).println()
}

private data class Game(
    val id: Int,
    val sets: List<GameSet>
) {
    data class GameSet(
        val red: Int,
        val green: Int,
        val blue: Int,
    ) {
        companion object {
            // 1 blue, 1 green, 1 red
            fun from(line: String): GameSet {
                val cubesByColor =
                    line
                        .split(", ")
                        .associate { draw ->
                            draw.split(" ")
                                .let { it.last() to it.first() }
                        }

                return GameSet(
                    red = cubesByColor["red"]?.toInt() ?: 0,
                    green = cubesByColor["green"]?.toInt() ?: 0,
                    blue = cubesByColor["blue"]?.toInt() ?: 0,
                )
            }
        }
    }

    companion object {
        private fun String.gameId(): Int =
            split(":")
                .first()
                .split(" ")
                .last()
                .toInt()

        // Game 70: 1 blue, 1 green; 1 red; 1 red, 2 blue, 1 green; 1 green, 2 red; 2 blue, 2 red; 1 red
        fun from(line: String): Game =
            Game(
                id = line.gameId(),
                sets = line
                    .split(": ")
                    .last()
                    .split("; ")
                    .map(GameSet::from)
            )
    }
}

private val Game.isPossible: Boolean
    get() = sets.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }

private val Game.power: Int
    get() = sets.maxOf(Game.GameSet::red) * sets.maxOf(Game.GameSet::green) * sets.maxOf(Game.GameSet::blue)
