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
                part = TestRunner.TestingPart.Part1,
                expectedResult = 13
            )

            solve()
        }
}

object Day04 : Solution {

    private const val WINNING_POINT_POWER = 2.0

    private fun String.getNumbers(): List<Int> = split(' ')
        .filterNot { it.isBlank() }
        .map { it.toInt() }

    override fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (_, card) = line.split("""Card\s+\d+:""".toRegex())

            val (winningCards, myCards) = card
                .split('|')
                .map { it.getNumbers() }

            val matches = winningCards.count { it in myCards }

            WINNING_POINT_POWER.pow((matches - 1).toDouble()).toInt()
        }
    }

    override fun part2(input: List<String>): Int {
        return input.size
    }
}