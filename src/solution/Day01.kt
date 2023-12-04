package solution

import println
import readInput

fun main() {
    val digitsStr = mapOf(
        "one" to 1, "two" to 2, "three" to 3, "four" to 4,
        "five" to 5, "six" to 6, "seven" to 7, "eight" to 8,
        "nine" to 9
    )

    fun String.getDigitWordToIntAt(index: Int): Int? {
        return digitsStr.entries
            .firstOrNull { (key, _) ->
                substring(
                    range = index..(index + 5).coerceAtMost(lastIndex)
                )
                    .startsWith(key)
            }
            ?.value
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }

            "$firstDigit$lastDigit".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigitInt = line.indexOfFirst { it.isDigit() }
            val firstDigitStr = line.indexOfAny(digitsStr.keys)

            val firstDigit = when {
                firstDigitStr < 0 || firstDigitInt in 0..<firstDigitStr -> line[firstDigitInt].digitToInt()
                else -> line.getDigitWordToIntAt(index = firstDigitStr) ?: 0
            }

            val lastDigitInt = line.indexOfLast { it.isDigit() }
            val lastDigitStr = line.lastIndexOfAny(digitsStr.keys)

            val lastDigit = when {
                lastDigitInt > lastDigitStr -> line[lastDigitInt].digitToInt()
                else -> line.getDigitWordToIntAt(index = lastDigitStr) ?: 0
            }

            "$firstDigit$lastDigit".toInt()
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
