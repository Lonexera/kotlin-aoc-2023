package solution

import TestRunner
import kotlin.math.pow

fun main() {
    TestRunner
        .create(
            testFileName = "Day04",
            solution = Day04
        )
        .run {
            check(
                fileName = "Day04_test",
                part = TestRunner.TestingPart.Part2,
                expectedResult = 30
            )

            solve()
        }
}

object Day04 : Solution {

    private const val WINNING_POINT_POWER = 2.0

    data class Card(
        val winningNumbers: List<Int>,
        val myNumbers: List<Int>,
    )

    private fun String.getNumbers(): List<Int> = split(' ')
        .filterNot { it.isBlank() }
        .map { it.toInt() }

    private fun String.parseToCard(): Card {
        val (cardIdStr, card) = split(':')

//        val cardId = cardIdStr.removePrefix("Card").trim().toInt()

        val (winningNumbers, myNumbers) = card
            .split('|')
            .map { it.getNumbers() }

        return Card(
            winningNumbers = winningNumbers,
            myNumbers = myNumbers
        )
    }

    override fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val card = line.parseToCard()

            val matches = card.winningNumbers.count { it in card.myNumbers }

            WINNING_POINT_POWER.pow((matches - 1).toDouble()).toInt()
        }
    }

    override fun part2(input: List<String>): Int {
        val wonCardIndexes = input.foldIndexed(initial = mutableListOf<Int>()) { cardIndex, acc, line ->
            val card = line.parseToCard()
            val matches = card.winningNumbers.count { it in card.myNumbers }

            val wonCardIndexes = buildList {
                repeat(acc.count { it == cardIndex } + 1) {
                    addAll(
                        elements = (cardIndex + 1)..(cardIndex + matches).coerceAtMost(input.lastIndex)
                    )
                }
            }

            acc.addAll(wonCardIndexes)
            acc
        }

        return (input.indices + wonCardIndexes)
            .groupBy { it }
            .map { (_, value) -> value.size }
            .sum()
    }
}