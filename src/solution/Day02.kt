package solution

import TestRunner

fun main() {
    TestRunner
        .create(
            testFileName = "Day02",
            solution = Day02
        )
        .run {
            check(
                fileName = "Day02_test",
                part = TestRunner.TestingPart.Part2,
                expectedResult = 2286
            )

            solve()
        }
}

object Day02 : Solution {

    data class CubesNumber(
        val red: Int,
        val green: Int,
        val blue: Int,
    ) {
        val all: Int get() = red + green + blue
    }

    data class Game(
        val id: Int,
        val sets: List<CubesNumber>,
    ) {
        fun isPossibleToPlay(baseline: CubesNumber): Boolean = sets.all { game ->
            game.all <= baseline.all &&
                    game.red <= baseline.red &&
                    game.green <= baseline.green &&
                    game.blue <= baseline.blue
        }
    }

    // Get number of cubes from formatted string - this should only contain a number
    // and cube color - no spaces, no delimeters
    private fun List<String>.getCubeNumber(colorName: String): Int =
        find { it.contains(colorName) }
            ?.takeWhile { it.isDigit() }
            ?.toInt()
            ?: 0

    private fun String.parseToGame(): Game {
        val gameIdStr = substringBefore(":")
        val gameStr = substringAfter(":")

        val gameId = gameIdStr.filter { it.isDigit() }.toInt()

        val sets = gameStr.split(";").map { setStr ->
            val cubesStrs = setStr
                .split(",")
                .map { it.trim() }

            CubesNumber(
                red = cubesStrs.getCubeNumber("red"),
                green = cubesStrs.getCubeNumber("green"),
                blue = cubesStrs.getCubeNumber("blue")
            )
        }

        return Game(
            id = gameId,
            sets = sets
        )
    }

    override fun part1(input: List<String>): Int {
        val possibleCubesNumber = CubesNumber(
            red = 12,
            green = 13,
            blue = 14
        )

        return input.asSequence()
            .map { it.parseToGame() }
            .filter { it.isPossibleToPlay(baseline = possibleCubesNumber) }
            .sumOf { it.id }
    }

    override fun part2(input: List<String>): Int {
        return input.asSequence()
            .map { it.parseToGame() }
            .sumOf { game ->
                val requiredRed = game.sets.maxOf { it.red }
                val requiredGreen = game.sets.maxOf { it.green }
                val requiredBlue = game.sets.maxOf { it.blue }

                requiredRed * requiredGreen * requiredBlue
            }
    }
}

