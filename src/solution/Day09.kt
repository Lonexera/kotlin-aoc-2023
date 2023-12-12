package solution

import TestRunner
import getAllSignedNumbers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    TestRunner
        .create(
            testFileName = "Day09",
            solution = Day09
        )
        .run {
            check(
                fileName = "Day09_test",
                part = TestRunner.TestingPart.Part1,
                expectedResult = 114
            )

            solve()
        }
}

object Day09 : Solution {
    
    private fun List<List<Int>>.extrapolateForward(): Int {
        return reversed()
            .fold(0) { acc, predictables ->
                acc + (predictables.lastOrNull() ?: 0)
            }
    }
    
    private fun List<Int>.differenciateSteps(): List<List<Int>> {
        var newList = this

        return buildList {
            add(this@differenciateSteps)

            while(!newList.all { it == 0 }) {
                newList = newList.zipWithNext().map { (first, second) -> second - first }

                add(newList)
            }
        }
    }
    
    private fun List<List<Int>>.extrapolateBackwards(): Int {
        return reversed()
            .fold(0) { acc, predictables ->
                (predictables.firstOrNull() ?: 0) - acc
            }
    }
    
    override fun part1(input: List<String>): Int {
        return runBlocking {
            input.map { line ->
                async {
                    line
                        .getAllSignedNumbers()
                        .differenciateSteps()
                        .extrapolateForward()
                }
            }
                .awaitAll()
                .sum()
        }
    }

    override fun part2(input: List<String>): Int {
        return runBlocking {
            input.map { line ->
                async {
                    line
                        .getAllSignedNumbers()
                        .differenciateSteps()
                        .extrapolateBackwards()
                }
            }
                .awaitAll()
                .sum()
        }
    }
}