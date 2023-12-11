import solution.Solution
import kotlin.system.measureTimeMillis

class TestRunner private constructor(
    private val testFileName: String,
    private val solution: Solution,
) {

    enum class TestingPart {
        Part1, Part2
    }

    fun check(
        fileName: String,
        part: TestingPart,
        expectedResult: Int,
    ) {
        // test if implementation meets criteria from the description, like:
        val testInput = readInput(fileName)

        val actualResult = when (part) {
            TestingPart.Part1 -> solution.part1(testInput)
            TestingPart.Part2 -> solution.part2(testInput)
        }

        println("\nCheck:")
        actualResult.println()
        check(actualResult == expectedResult)
    }

    fun solve() {
        val input = readInput(testFileName)

        println("\nPart 1:")
        val part1Time = measureTimeMillis {
            solution.part1(input).println()
        }
        println("(time: ${part1Time}ms)")
        println("\nPart 2:")
        val part2Time = measureTimeMillis {
            solution.part2(input).println()
        }
        println("(time: ${part2Time}ms)")
    }

    companion object {
        fun create(
            testFileName: String,
            solution: Solution,
        ): TestRunner =
            TestRunner(
                testFileName = testFileName,
                solution = solution,
            )
    }
}