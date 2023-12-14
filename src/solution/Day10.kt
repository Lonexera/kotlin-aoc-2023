package solution

import TestRunner
import java.util.LinkedList

fun main() {
    TestRunner
        .create(
            testFileName = "Day10",
            solution = Day10
        )
        .run {
            check(
                fileName = "Day10_test",
                part = TestRunner.TestingPart.Part1,
                expectedResult = 8
            )

            solve()
        }
}

private sealed class Tile {
    object Start : Tile() { override fun toString(): String = "S" }
    object Ground : Tile() { override fun toString(): String = "." }

    sealed class PipePart : Tile() {
        object NorthSouth : PipePart() { override fun toString(): String = "|" }
        object NorthWest : PipePart() { override fun toString(): String = "J" }
        object NorthEast : PipePart() { override fun toString(): String = "L" }
        object SouthWest : PipePart() { override fun toString(): String = "7" }
        object SouthEast : PipePart() { override fun toString(): String = "F" }
        object WestEast : PipePart() { override fun toString(): String = "-" }
    }

    companion object {
        fun fromSymbol(symbol: Char): Tile =
            when (symbol) {
                'S' -> Start
                '.' -> Ground
                '|' -> PipePart.NorthSouth
                'J' -> PipePart.NorthWest
                'L' -> PipePart.NorthEast
                '7' -> PipePart.SouthWest
                'F' -> PipePart.SouthEast
                '-' -> PipePart.WestEast
                else -> error("Tile '${symbol}' is undefined!")
            }
    }
}

object Day10 : Solution {

    override fun part1(input: List<String>): Int {
        return input.findMainLoop().size / 2
    }

    override fun part2(input: List<String>): Int {
        return input.size
    }
}

private data class TileInfo(
    val tile: Tile,
    val x: Int,
    val y: Int,
)

private fun List<String>.findStart(): TileInfo {
    val startLine = first { START in it }
    val y = indexOf(startLine)
    val x = startLine.indexOf(START)

    return TileInfo(x = x, y = y, tile = Tile.Start)
}

private fun List<String>.getTileOn(x: Int, y: Int): TileInfo? {
    return getOrNull(y)?.getOrNull(x)
        ?.let { symbol ->
            TileInfo(x = x, y = y, tile = Tile.fromSymbol(symbol))
        }
}

private fun List<String>.findStartPipes(
    start: TileInfo,
): Pair<TileInfo, TileInfo> {
    val northPipe = this.getOrNull(start.y - 1)?.get(start.x)
        ?.let { Tile.fromSymbol(it) }
        ?.takeIf { tile ->
            when (tile) {
                Tile.PipePart.NorthSouth, Tile.PipePart.SouthWest, Tile.PipePart.SouthEast -> true
                else -> false
            }
        }
        ?.let { TileInfo(x = start.x, y = start.y - 1, tile = it) }

    val southPipe = this.getOrNull(start.y + 1)?.get(start.x)
        ?.let { Tile.fromSymbol(it) }
        ?.takeIf { tile ->
            when (tile) {
                Tile.PipePart.NorthEast, Tile.PipePart.NorthWest, Tile.PipePart.NorthSouth -> true
                else -> false
            }
        }
        ?.let { TileInfo(x = start.x, y = start.y + 1, tile = it) }

    val westPipe = this[start.y].getOrNull(start.x - 1)
        ?.let { Tile.fromSymbol(it) }
        ?.takeIf { tile ->
            when (tile) {
                Tile.PipePart.NorthEast, Tile.PipePart.WestEast, Tile.PipePart.SouthEast -> true
                else -> false
            }
        }
        ?.let { TileInfo(x = start.x - 1, y = start.y, tile = it) }

    val eastPipe = this[start.y].getOrNull(start.x + 1)
        ?.let { Tile.fromSymbol(it) }
        ?.takeIf { tile ->
            when (tile) {
                Tile.PipePart.WestEast, Tile.PipePart.SouthWest, Tile.PipePart.NorthWest -> true
                else -> false
            }
        }
        ?.let { TileInfo(x = start.x + 1, y = start.y, tile = it) }

    return listOfNotNull(northPipe, southPipe, westPipe, eastPipe).take(2).zipWithNext().first()
}

private fun TileInfo.findAdjacentPipes(map: List<String>): List<TileInfo> {
    return buildList {
        when (tile) {
            Tile.PipePart.NorthSouth -> {
                add(map.getTileOn(x = x, y = y - 1))
                add(map.getTileOn(x = x, y = y + 1))
            }
            Tile.PipePart.NorthWest -> {
                add(map.getTileOn(x = x, y = y - 1))
                add(map.getTileOn(x = x - 1, y = y))
            }
            Tile.PipePart.NorthEast -> {
                add(map.getTileOn(x = x, y = y - 1))
                add(map.getTileOn(x = x + 1, y = y))
            }
            Tile.PipePart.SouthWest -> {
                add(map.getTileOn(x = x, y = y + 1))
                add(map.getTileOn(x = x - 1, y = y))
            }
            Tile.PipePart.SouthEast -> {
                add(map.getTileOn(x = x, y = y + 1))
                add(map.getTileOn(x = x + 1, y = y))
            }
            Tile.PipePart.WestEast -> {
                add(map.getTileOn(x = x - 1, y = y))
                add(map.getTileOn(x = x + 1, y = y))
            }
            else -> Unit
        }
    }
        .filterNotNull()
}

private fun LinkedList<TileInfo>.findNextFirst(
    map: List<String>
): TileInfo {
    val currentNode = first
    val previousNode = get(1)
    
    val next = currentNode.findAdjacentPipes(map)
        .filterNot { it == previousNode }
        .first()

    return next
}

private fun LinkedList<TileInfo>.findNextLast(
    map: List<String>
): TileInfo {
    val currentNode = last
    val previousNode = get(lastIndex - 1)

    val next = currentNode.findAdjacentPipes(map)
        .filterNot { it == previousNode }
        .first()

    return next
}

private fun List<String>.findMainLoop(): LinkedList<TileInfo> {
    val list = LinkedList<TileInfo>()

    val startNode = findStart()
    list.add(startNode)

    val (left, right) = findStartPipes(startNode)
    list.addFirst(left)
    list.addLast(right)

    while (true) {
        val first = list.findNextFirst(map = this)
        if (list.contains(first)) break
        list.addFirst(first)
        
        val last = list.findNextLast(map = this)
        if (list.contains(last)) break
        list.addLast(last)
    }
    
    return list
}

private const val START = 'S'