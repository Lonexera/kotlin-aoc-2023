package solution

import TestRunner
import getAllBigIntegers
import java.math.BigInteger

fun main() {
    TestRunner
        .create(
            testFileName = "Day05",
            solution = Day05
        )
        .run {
            check(
                fileName = "Day05_test",
                part = TestRunner.TestingPart.Part1,
                expectedResult = 35
            )

            solve()
        }
}

object Day05 : Solution {

    private val bigOne = BigInteger.ONE

    private val String.hasDigits: Boolean get() = any { it.isDigit() }

    private fun List<String>.getMaps(): List<Map<ClosedRange<BigInteger>, ClosedRange<BigInteger>>> {
        var currentIndex = 0
        val linesSize = size

        return buildList {
            while (currentIndex < linesSize) {
                val currentLine = this@getMaps[currentIndex]

                currentIndex += when {
                    currentLine.hasDigits -> {
                        val map = this@getMaps.subList(currentIndex, linesSize)
                            .takeWhile { it.hasDigits }
                            .associate { line ->
                                val (destinationStart, sourceStart, rangeLength) = line.getAllBigIntegers()

                                sourceStart..(sourceStart + rangeLength - bigOne) to
                                    destinationStart..(destinationStart + rangeLength - bigOne)
                            }
                        add(map)

                        map.size
                    }

                    else -> 1
                }
            }
        }
    }

    private fun transform(
        seeds: List<BigInteger>,
        maps: List<Map<ClosedRange<BigInteger>, ClosedRange<BigInteger>>>
    ): List<BigInteger> {
        return maps.fold(seeds) { accumulatorSeeds, map ->
            accumulatorSeeds.map { seed ->
                map.entries.find { (sourceRange, _) -> seed in sourceRange }
                    ?.let { (sourceRange, destinationRange) ->
                        val sourceIndex = seed - sourceRange.start
                        destinationRange.start + sourceIndex
                    }
                    ?: seed
            }
        }
    }

    override fun part1(input: List<String>): Int {
        val seeds = input.first().getAllBigIntegers()

        return input
            .takeLast(input.size - 2)
            .getMaps()
            .run {
                transform(seeds = seeds, maps = this)
            }
            .minOrNull()
            ?.toInt()
            ?: 0
    }

    override fun part2(input: List<String>): Int {
        val seeds = input.first().getAllBigIntegers()
            .chunked(2)
            .flatMap { (start, rangeLength) ->
                buildList {
                    repeat(rangeLength.toInt()) { index ->
                        add(start + index.toBigInteger())
                    }
                }
            }
            .also { println(it.size) }

        return input
            .takeLast(input.size - 2)
            .getMaps()
            .run {
                transform(seeds = seeds, maps = this)
            }
            .minOrNull()
            ?.toInt()
            ?: 0
    }
}