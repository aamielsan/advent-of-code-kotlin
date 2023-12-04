import kotlin.math.abs

fun main() {
    val test: List<String> = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
    """.trimIndent().lines()

    fun part1(input: List<String>): Int {
        val schematic = Schematic.from(input)
        return schematic
            .partNumbers
            .filter { it isAdjacentToAnyOf schematic.symbols }
            .sumOf(Schematic.PartNumber::value)
    }

    fun part2(input: List<String>): Int  {
        fun Schematic.Symbol.isGear(): Boolean = value == "*"
        fun Schematic.PartNumber.gearRatio(other: Schematic.PartNumber): Int = this.value * other.value

        val schematic = Schematic.from(input)

        return schematic.symbols
            .filter(Schematic.Symbol::isGear)
            .sumOf { gear ->
                val adjacentPartNumbers = schematic.partNumbers.filter { it isAdjacentTo gear }
                when (adjacentPartNumbers.size) {
                    2 -> adjacentPartNumbers.first().gearRatio(adjacentPartNumbers.last())
                    else -> 0
                }
            }
    }

    val input: List<String> = inputAsLines("Day03")

    check(part1(test).println() == 4361)
    part1(input).println()

    check(part2(test).println() == 467835)
    part2(input).println()
}

private fun String.isNumeric() = isNotEmpty() && all(Char::isDigit)

private data class Schematic(
    private val _partNumbers: MutableList<PartNumber> = mutableListOf(),
    private val _symbols: MutableList<Symbol> = mutableListOf()
) {
    data class Point(val x: Int, val y: Int) {
        infix fun isAdjacentTo(another: Point): Boolean =
            abs(x - another.x) <= 1 && abs(y - another.y) <= 1
    }

    data class PartNumber(
        val value: Int,
        val points: List<Point>
    ) {
        infix fun isAdjacentTo(symbol: Symbol): Boolean = points.any { it isAdjacentTo symbol.point }
        infix fun isAdjacentToAnyOf(symbols: List<Symbol>): Boolean = symbols.any { this isAdjacentTo it }
    }

    data class Symbol(
        val value: String,
        val point: Point,
    )

    companion object

    val partNumbers: List<PartNumber>
        get() = _partNumbers

    val symbols: List<Symbol>
        get() = _symbols

    fun partNumber(partNumber: PartNumber) = apply { _partNumbers.add(partNumber) }

    fun symbol(symbol: Symbol) = apply { _symbols.add(symbol) }
}

private val regex: Regex = """\d+|[^0-9.]+|\.+""".toRegex()

private fun Schematic.Companion.from(input: List<String>): Schematic =
    Schematic().also { schematic ->
        input
            .forEachIndexed { yIndex, line ->
                regex
                    .findAll(line)
                    .forEach { match ->
                        when {
                            match.value.startsWith(".") -> Unit
                            match.value.isNumeric() -> schematic.partNumber(
                                Schematic.PartNumber(
                                    value = match.value.toInt(),
                                    points = match.range.map { xIndex -> Schematic.Point(x = xIndex, y = yIndex) }
                                )
                            )
                            else -> schematic.symbol(
                                Schematic.Symbol(
                                    value = match.value,
                                    point = Schematic.Point(x = match.range.first(), y = yIndex)
                                )
                            )
                        }
                    }
            }
    }
