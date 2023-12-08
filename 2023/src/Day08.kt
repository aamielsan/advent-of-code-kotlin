fun main() {
    val test = """
        LLR

        BBB = (AAA, ZZZ)
        AAA = (BBB, BBB)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()

    fun part1(input: List<String>): Int {
        val steps = input.first()
        val mappings = input
            .drop(2)
            .fold(emptyMap<String, Map<Char, String>>()) { acc, line ->
                val (from, directionRaw) =
                    line
                        .replace("\\(|\\)|,".toRegex(), "")
                        .split(" = ")

                val directions = directionRaw.split(" ").let { mapOf('L' to it[0], 'R' to it[1]) }
                acc + mapOf(from to directions)
            }

        tailrec fun navigateAndCountFrom(node: String, count: Int = 0): Int =
            when (node) {
                "ZZZ" -> count
                else -> navigateAndCountFrom(mappings[node]?.get(steps[count % steps.length])!!, count + 1)
            }

        return navigateAndCountFrom("AAA")
    }

    fun part2(input: List<String>): Long {
        val steps = input.first()
        val mappings = input
            .drop(2)
            .fold(emptyMap<String, Map<Char, String>>()) { acc, line ->
                val (from, directionRaw) =
                    line
                        .replace("\\(|\\)|,".toRegex(), "")
                        .split(" = ")

                val directions = directionRaw.split(" ").let { mapOf('L' to it[0], 'R' to it[1]) }
                acc + mapOf(from to directions)
            }

        tailrec fun navigateAndCountFrom(nodes: List<String>, count: Long = 0L): Long =
            when {
                nodes.println().all { it.endsWith('Z') } -> count
                else -> navigateAndCountFrom(
                    nodes.map { mappings[it]!![steps[(count % steps.length).toInt()]]!! },
                    count = count + 1
                )
            }

        return navigateAndCountFrom(
            nodes = mappings.keys.filter { it.endsWith('A') }
        )
    }

    val input: List<String> = inputAsLines("Day08")

    check(part1(test) == 6)
    part1(input).println()
    check(part2(
        """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
        """.trimIndent().lines()
    ) == 6L)
    part2(input).println()
}
