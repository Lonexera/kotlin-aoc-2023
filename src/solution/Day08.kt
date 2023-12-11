package solution

import TestRunner

fun main() {
    TestRunner
        .create(
            testFileName = "Day08",
            solution = Day08
        )
        .run {
            check(
                fileName = "Day08_test",
                part = TestRunner.TestingPart.Part1,
                expectedResult = 6
            )

            solve()
        }
}

object Day08 : Solution {
    private const val START = "AAA"
    private const val END = "ZZZ"
    
    enum class Direction { Left, Right }
    
    private fun String.getInstructions(): List<Direction> =
        map {
            when (it) {
                'L' -> Direction.Left
                'R' -> Direction.Right
                else -> error("Unclear direction '${it}'!")
            }
        }
    
    private fun List<String>.findPath(path: String): Pair<String, String> {
        val pathString = find { it.take(3) == path } ?: error("No path '${path}' found!")
        
        val (left, right) = pathString
            .dropWhile { it != '(' }
            .drop(1)
            .dropLastWhile { it != ')' }
            .dropLast(1)
            .split(", ")
        
        return left to right
    }
    
    override fun part1(input: List<String>): Int {
        val instructions = input.first().getInstructions()
        val paths = input.drop(2)
        
        var steps = 0
        var startPaths = paths.findPath(START)
        
        while (true) {
            instructions.forEach { direction ->  
                val newPath = when (direction) {
                    Direction.Left -> startPaths.left
                    Direction.Right -> startPaths.right
                }
                
                steps++
                
                if (newPath == END) {
                    return steps
                }
                
                startPaths = paths.findPath(newPath)
            }
        }
    }

    override fun part2(input: List<String>): Int {
        return input.size
    }
}

private val Pair<String, String>.left: String get() = first
private val Pair<String, String>.right: String get() = second