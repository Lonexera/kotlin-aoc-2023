import solution.Solution

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
        solution.part1(input).println()
        println("\nPart 2:")
        solution.part2(input).println()
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