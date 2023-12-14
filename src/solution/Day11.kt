package solution

import TestRunner
import println
import kotlin.math.abs

fun main() {
    TestRunner
        .create(
            testFileName = "Day11",
            solution = Day11
        )
        .run {
            check(
                fileName = "Day11_test",
                part = TestRunner.TestingPart.Part1,
                expectedResult = 374
            )

            solve()
        }
}

object Day11 : Solution {
    private const val GALAXY = '#'
    
    data class Galaxy(
        val x: Int,
        val y : Int,
    )
    
    private fun List<String>.expandedUniverse(): List<String> {
        val indexesExpanding = List(first().length) { index ->
            val needsExpanding = map { it[index] }.none { it == GALAXY }
            if (needsExpanding) index else null
        }
            .filterNotNull()
            
        return flatMap { row ->
            val newRow = row.doubleCharsAt(indexesExpanding)
            
            if (newRow.none {it == GALAXY }) {
                listOf(newRow, newRow)
            } else {
                listOf(newRow)
            }
        }
    }
    
    private fun String.doubleCharsAt(indexes: List<Int>): String {
        return flatMapIndexed { index, char ->
            if (index in indexes) {
                listOf(char, char)
            } else {
                listOf(char)
            }
        }
            .joinToString("")
    }
    
    private fun List<String>.findAllGalaxies(): List<Galaxy> {
        return this@findAllGalaxies.mapIndexed { y, row ->
                row.mapIndexedNotNull { x, point ->
                    if (point == GALAXY) {
                        Galaxy(x = x, y = y)
                    } else {
                        null
                    }
                }
            }
                .filterNot { it.isEmpty() }
                .flatten()
    }
    
    private fun <T> List<T>.uniquePairs(): List<Pair<T, T>> {
        return flatMapIndexed { index, value ->
            drop(index + 1).map { value to it }
        }
    }
    
    override fun part1(input: List<String>): Int {
        return input
            .expandedUniverse()
            .findAllGalaxies()
            .uniquePairs()
            .sumOf { (first, second) ->
                abs(first.x - second.x) + abs(first.y - second.y)
            }
    }

    override fun part2(input: List<String>): Int {
        return input.size
    }
}