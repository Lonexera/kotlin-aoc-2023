package solution

import TestRunner
import getAllLongs

fun main() {
    TestRunner
        .create(
            testFileName = "Day06",
            solution = Day06
        )
        .run {
            check(
                fileName = "Day06_test",
                part = TestRunner.TestingPart.Part2,
                expectedResult = 71503
            )

            solve()
        }
}

object Day06 : Solution {

    private data class Race(
        val timeMs: Long,
        val recordDistanceMl: Long,
    )

    private fun Race.winningVariations(): List<Long> {
        return buildList {
            for (speed in 0..timeMs) {
                val leftTime = timeMs - speed
                val madeDistance = leftTime * speed

                if (madeDistance > recordDistanceMl) {
                    add(madeDistance)
                }
            }
        }
    }
    
    override fun part1(input: List<String>): Int {
        val times = input[0].getAllLongs()
        val distances = input[1].getAllLongs()
        
        return times.mapIndexed { index, raceTime ->
            Race(
                timeMs = raceTime,
                recordDistanceMl = distances[index]
            )
        }
            .map { it.winningVariations().size }
            .fold(1) { acc, variationsNumber ->
                acc * variationsNumber
            }
    }
    
    private fun Race.winningVariationsCount(): Long {
        var count = 0L
        
        for (speed in 0..timeMs) {
            val leftTime = timeMs - speed
            val madeDistance = leftTime * speed

            if (madeDistance > recordDistanceMl) {
                count++
            }
        }
        
        return count
    }

    override fun part2(input: List<String>): Int {
        val race = Race(
            timeMs = input[0].filter { it.isDigit() }.toLong(),
            recordDistanceMl = input[1].filter { it.isDigit() }.toLong()
        )
        
        return race.winningVariationsCount().toInt()
    }
}