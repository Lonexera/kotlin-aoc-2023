package solution

import TestRunner
import kotlin.math.pow

fun main() {
    TestRunner
        .create(
            testFileName = "Day07",
            solution = Day07
        )
        .run {
            check(
                fileName = "Day07_test",
                part = TestRunner.TestingPart.Part1,
                expectedResult = 6440
            )

            solve()
        }
}

object Day07 : Solution {
    
    private const val TWENTY_D = 20.0
    
    private val cardStrength: List<Char> = listOf(
        '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'
    )
    
    private enum class HandType(val value: Int) {
        FiveOfKind(7),
        FourOfKind(6),
        FullHouse( 5),
        ThreeOfKind(4),
        TwoPair(3),
        OnePair(2),
        HighCard(1),
        None(0),
    }
    
    private data class Hand(
        val cards: CharSequence,
        val bid: Int,
        val handType: HandType,
    )
    
    private fun String.parseToHand(): Hand {
        val (cards, bid) = split(' ')
        
        val combinations = cards
            .groupBy { it }
            .map { (card, sameCards)  -> card to sameCards.size }
            .sortedByDescending { it.second }
        
        val handType = when {
            combinations[0].second == 5 -> HandType.FiveOfKind
            combinations[0].second == 4 -> HandType.FourOfKind
            combinations[0].second == 3 && combinations[1].second == 2 -> HandType.FullHouse
            combinations[0].second == 3 -> HandType.ThreeOfKind
            combinations[0].second == 2 && combinations[1].second == 2 -> HandType.TwoPair
            combinations[0].second == 2 -> HandType.OnePair
            cards.toCharArray().distinct().size == 5 -> HandType.HighCard
            else -> HandType.None
        }
        
        return Hand(
            cards = cards,
            bid = bid.toInt(),
            handType = handType
        )
    }
    
    private fun Hand.getStrength(): Int =
        cards.foldIndexed(0) { index, acc, card ->
            (acc + cardStrength.indexOf(card) * TWENTY_D.pow(cards.lastIndex - index)).toInt()
        }
    
    override fun part1(input: List<String>): Int {
        return input.asSequence()
            .map { it.parseToHand() }
            .sortedBy { it.handType.value }
            .groupBy { it.handType.value }
            .flatMap { (_, hands) ->
                hands.sortedBy { it.getStrength() }
            }
            // sorted from minimum to maximum
            .foldIndexed(0) { index, acc, hand ->
                acc + hand.bid * (index + 1)
            }
    }

    override fun part2(input: List<String>): Int {
        return input.size
    }
}