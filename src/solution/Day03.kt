package solution

import TestRunner

fun main() {
    TestRunner
        .create(
            testFileName = "Day03",
            solution = Day03
        )
        .run {
            check(
                fileName = "Day03_test",
                part = TestRunner.TestingPart.Part2,
                expectedResult = 467835
            )

            solve()
        }
}

object Day03 : Solution {

    private const val GEAR: Char = '*'

    data class NumberCoordinates(
        val number: Int,
        val y: Int,
        val startX: Int,
        val endX: Int,
    )

    data class SymbolCoordinates(
        val symbol: Char,
        val x: Int,
        val y: Int,
    )

    private val Char.isSymbol: Boolean get() = !(isDigit() || this == '.')

    private fun List<String>.findSymbols(): List<SymbolCoordinates> =
        flatMapIndexed { yIndex, line ->
            line.mapIndexedNotNull { xIndex, char ->
                char
                    .takeIf { it.isSymbol }
                    ?.let { SymbolCoordinates(symbol = it, x = xIndex, y = yIndex) }
            }
        }

    private fun List<String>.findNumbers(): List<NumberCoordinates> =
        flatMapIndexed { yIndex, line ->
            var currentX = 0
            val numbers = mutableListOf<NumberCoordinates>()

            while (currentX < line.length) {
                currentX += when {
                    line[currentX].isDigit() -> {
                        val number = line.substring(startIndex = currentX).takeWhile { it.isDigit() }

                        numbers += NumberCoordinates(
                            number = number.toInt(),
                            y = yIndex,
                            startX = currentX,
                            endX = currentX + number.length - 1
                        )

                        number.length
                    }

                    else -> 1
                }
            }

            numbers
        }

    private fun NumberCoordinates.isAdjacentToAny(symbols: List<SymbolCoordinates>): Boolean =
        symbols.any { symbol ->
            val symbolRangeX = (symbol.x - 1)..(symbol.x + 1)
            val symbolRangeY = (symbol.y - 1)..(symbol.y + 1)

            y in symbolRangeY && (startX..endX).intersects(symbolRangeX)
        }

    override fun part1(input: List<String>): Int {
        val symbols = input.findSymbols()
        val numbers = input.findNumbers()

        return numbers
            .filter { it.isAdjacentToAny(symbols) }
            .distinct()
            .sumOf { it.number }
    }

    private fun SymbolCoordinates.findAdjacentNumbers(numbers: List<NumberCoordinates>): List<NumberCoordinates> =
        numbers.filter { number ->
            val symbolRangeX = (x - 1)..(x + 1)
            val symbolRangeY = (y - 1)..(y + 1)

            number.y in symbolRangeY && (number.startX..number.endX).intersects(symbolRangeX)
        }

    override fun part2(input: List<String>): Int {
        val gears = input.findSymbols().filter { it.symbol == GEAR }
        val numbers = input.findNumbers()

        return gears
            .map { it.findAdjacentNumbers(numbers) }
            .filter { it.size == 2 }
            .sumOf { adjacentNumbers ->
                adjacentNumbers.fold(1) { acc, number -> acc * number.number }.toInt()
            }
    }
}

private fun IntRange.intersects(other: IntRange): Boolean = any { it in other }
