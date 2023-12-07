fun main() {
    val test = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent().lines()

    fun part1(input: List<String>): Int {
        val cards = "23456789TJQKA" // sorted in asc

        return input
            .map { line ->
                val (rawHand, rawBid) = line.split(" ")
                Hand.from(
                    cards = rawHand.map { cards.indexOf(it) },
                    bid = rawBid.toInt(),
                )
            }
            .sorted()
            .mapIndexed { index, hand -> index.inc() * hand.bid }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val mapping = "J23456789TQKA" // sorted in asc
        return input
            .map { line ->
                val (rawHand, rawBid) = line.split(" ")
                val cards = rawHand.map { mapping.indexOf(it) }
                HandWithJoker.from(
                    bid = rawBid.toInt(),
                    cards = cards,
                )
            }
            .sorted()
            .mapIndexed { index, hand -> index.inc() * hand.bid }
            .sum()
    }

    val input: List<String> = inputAsLines("Day07")

    check(part1(test) == 6440)
    part1(input).println()
    check(part2(test) == 5905)
    part2(input).println()
}

private data class Hand(
    val bid: Int,
    private val cards: List<Int>,
    private val type: Type,
): Comparable<Hand> {
    enum class Type {
        HighCard,
        OnePair,
        TwoPair,
        ThreeOfAKind,
        FullHouse,
        FourOfAKind,
        FiveOfAKind;

        companion object {
            fun from(countPerCard: List<Pair<Int, Int>>): Type =
                when {
                    countPerCard.size == 1 -> FiveOfAKind
                    countPerCard.size == 2 && countPerCard[0].second == 4 -> FourOfAKind
                    countPerCard.size == 2 && countPerCard[0].second == 3 -> FullHouse
                    countPerCard.size == 3 && countPerCard[0].second == 3 -> ThreeOfAKind
                    countPerCard.size == 3 && countPerCard[0].second == 2 -> TwoPair
                    countPerCard.size == 4 -> OnePair
                    else -> HighCard
                }
        }
    }

    companion object {
        // part 1
        fun from(
            cards: List<Int>,
            bid: Int,
        ): Hand {
            val countPerCard =
                cards
                    .groupingBy { it }
                    .eachCount()
                    .toList()
                    // if we want to play by the poker rules where comparison are done by type then card
                    // .sortedWith(compareBy({ (_, type) -> -type }, { (card) -> -card }))
                    .sortedByDescending { it.second }

            return from(
                bid = bid,
                cards = cards,
                countPerCard = countPerCard,
            )
        }

        fun from(
            bid: Int,
            cards: List<Int>,
            countPerCard: List<Pair<Int, Int>>,
        ): Hand =
            Hand(
                bid = bid,
                cards = cards,
                type = Type.from(countPerCard)
            )
    }

    override fun compareTo(other: Hand): Int {
        val comparison = type.compareTo(other.type)

        if (comparison != 0) return comparison

        cards.forEachIndexed { index, thisCard ->
            val otherCard = other.cards[index]
            if (thisCard < otherCard) return -1
            if (thisCard > otherCard) return 1
        }

        return 0
    }
}

private data class HandWithJoker(
    val bid: Int,
    private val cards: List<Int>,
    private val type: Type,
): Comparable<HandWithJoker> {
    enum class Type {
        HighCard,
        JokerOnePair,
        OnePair,
        TwoPair,
        JokerThreeOfAKind,
        ThreeOfAKind,
        JokerFullHouse,
        FullHouse,
        JokerFourOfAKind,
        FourOfAKind,
        JokerFiveOfAKind,
        FiveOfAKind;

        companion object {
            fun from(countPerCard: List<Pair<Int, Int>>): Type {
                val jokerCount = countPerCard.firstOrNull { it.first == 0 }?.second ?: 0
                return if (jokerCount == 0) {
                    // as is
                    when {
                        countPerCard.size == 1 -> FiveOfAKind
                        countPerCard.size == 2 && countPerCard[0].second == 4 -> FourOfAKind
                        countPerCard.size == 2 && countPerCard[0].second == 3 -> FullHouse
                        countPerCard.size == 3 && countPerCard[0].second == 3 -> ThreeOfAKind
                        countPerCard.size == 3 && countPerCard[0].second == 2 -> TwoPair
                        countPerCard.size == 4 -> OnePair
                        else -> HighCard
                    }
                } else {
                    when {
                        countPerCard.size == 1 -> JokerFiveOfAKind
                        countPerCard.size == 2 -> JokerFiveOfAKind

                        countPerCard.size == 3 && jokerCount == 3 && countPerCard[0].second == 1 -> JokerFourOfAKind
                        countPerCard.size == 3 && jokerCount == 2 && countPerCard[0].second == 2 -> JokerFourOfAKind
                        countPerCard.size == 3 && jokerCount == 1 && countPerCard[0].second == 3 -> JokerFourOfAKind

                        countPerCard.size == 3 && jokerCount == 1 && countPerCard[0].second == 2 -> JokerFullHouse

                        countPerCard.size == 4 && jokerCount == 2 && countPerCard[0].second == 1 -> JokerThreeOfAKind
                        countPerCard.size == 4 && jokerCount == 1 && countPerCard[0].second == 2 -> JokerThreeOfAKind

                        else -> JokerOnePair
                    }
                }
            }
        }
    }

    companion object {
        fun from(
            cards: List<Int>,
            bid: Int,
        ): HandWithJoker {
            val countPerCard =
                cards
                    .groupingBy { it }
                    .eachCount()
                    .toList()
                    .sortedByDescending { it.second }

            return HandWithJoker(
                bid = bid,
                cards = cards,
                type = Type.from(countPerCard),
            )
        }
    }

    override fun compareTo(other: HandWithJoker): Int {
        val comparison = type.compareTo(other.type)

        if (comparison != 0) return comparison

        cards.forEachIndexed { index, thisCard ->
            val otherCard = other.cards[index]
            if (thisCard < otherCard) return -1
            if (thisCard > otherCard) return 1
        }

        return 0
    }
}
