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
                part = TestRunner.TestingPart.Part2,
                expectedResult = 5905
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

    private val cardStrength2: List<Char> = listOf(
        'J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'
    )

    private fun Hand.getStrength2(): Int =
        cards.foldIndexed(0) { index, acc, card ->
            (acc + cardStrength2.indexOf(card) * TWENTY_D.pow(cards.lastIndex - index)).toInt()
        }

    private fun String.parseToHandPart2(): Hand {
        val (cards, bid) = split(' ')

        val combinations = cards
            .filterNot { it == 'J' }
            .groupBy { it }
            .map { (card, sameCards)  -> card to sameCards.size }
            .sortedByDescending { it.second }

        val jokers = cards.filter { it == 'J' }

        val handType = when {
            jokers.length == 5 -> HandType.FiveOfKind
            combinations[0].second + jokers.length >= 5 -> HandType.FiveOfKind
            combinations[0].second + jokers.length >= 4 -> HandType.FourOfKind
            combinations[0].second == 3 && combinations[1].second == 2 -> HandType.FullHouse
            combinations[0].second == 2 && combinations[1].second == 2 && jokers.length == 1 -> HandType.FullHouse
            combinations[0].second + jokers.length >= 3 -> HandType.ThreeOfKind
            combinations[0].second == 2 && combinations[1].second == 2 -> HandType.TwoPair
            combinations[0].second + jokers.length >= 2 -> HandType.OnePair
            cards.toCharArray().distinct().size == 5 -> HandType.HighCard
            else -> HandType.None
        }

        return Hand(
            cards = cards,
            bid = bid.toInt(),
            handType = handType
        )
    }

    override fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { it.parseToHandPart2() }
            .sortedBy { it.handType.value }
            .groupBy { it.handType.value }
            .flatMap { (_, hands) ->
                hands.sortedBy { it.getStrength2() }
            }
            // sorted from minimum to maximum
            .foldIndexed(0) { index, acc, hand ->
                acc + hand.bid * (index + 1)
            }
    }
}