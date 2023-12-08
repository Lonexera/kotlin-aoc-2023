package solution

import TestRunner
import getAllLongs
import kotlinx.coroutines.*

fun main() {
    TestRunner
        .create(
            testFileName = "Day05",
            solution = Day05
        )
        .run {
            check(
                fileName = "Day05_test",
                part = TestRunner.TestingPart.Part2,
                expectedResult = 46
            )

            solve()
        }
}

object Day05 : Solution {

    private val String.hasDigits: Boolean get() = any { it.isDigit() }

    private fun List<String>.getMaps(): List<Map<LongRange, LongRange>> {
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
                                val (destinationStart, sourceStart, rangeLength) = line.getAllLongs()

                                sourceStart..<sourceStart + rangeLength to
                                        destinationStart..<destinationStart + rangeLength
                            }
                        add(map)

                        map.size
                    }

                    else -> 1
                }
            }
        }
    }

    private suspend fun transform(
        seeds: List<Long>,
        maps: List<Map<LongRange, LongRange>>
    ): List<Long> = coroutineScope {
        maps.fold(seeds) { accumulatorSeeds, map ->
            accumulatorSeeds.map { seed ->
                async {
                    map.entries.find { (sourceRange, _) -> seed in sourceRange }
                        ?.let { (sourceRange, destinationRange) ->
                            val sourceIndex = seed - sourceRange.first
                            destinationRange.first + sourceIndex
                        }
                            ?: seed
                }
            }
                .awaitAll()
        }
    }

    override fun part1(input: List<String>): Int {
        val seeds = input.first().getAllLongs()

        return input
            .takeLast(input.size - 2)
            .getMaps()
            .run {
                runBlocking { transform(seeds = seeds, maps = this@run) }
            }
            .minOrNull()
            ?.toInt()
            ?: 0
    }

    private suspend fun transformRanges(
        seedRanges: List<LongRange>,
        maps: List<Map<LongRange, LongRange>>
    ): List<Long> = coroutineScope {
        seedRanges.map { seeds ->
            async {
                transform(
                    seeds = seeds.toList(),
                    maps = maps
                )
                    .asSequence()
                    .min()
            }
        }
            .awaitAll()
    }

    override fun part2(input: List<String>): Int {
        val seedRanges = input.first().getAllLongs()
            .chunked(2)
            .map { (start, length) -> start..<start + length }

        return input
            .takeLast(input.size - 2)
            .getMaps()
            .run {
                runBlocking(Dispatchers.Default) { transformRanges(seedRanges = seedRanges, maps = this@run) }
            }
            .minOrNull()
            ?.toInt()
            ?: 0
    }
}