package org.firstinspires.ftc.teamcode.modular

import java.util.PriorityQueue
import org.firstinspires.ftc.teamcode.modular.Vector2

class PathBuilder(val gridWidth: Int, val gridHeight: Int) {
    class Cell (
        var parentRow: Int? = null,
        var parentCol: Int? = null,
        val row: Int,
        val col: Int,
        var distanceCost: Int = INF,
        var heuristicCost: Int = 0,
        var visited: Boolean = false,
        var blocked: Boolean = false
        ) : Comparable<Cell> {

        companion object {
            const val INF = Int.MAX_VALUE
        }

        val totalCost: Int
            get() = distanceCost + heuristicCost

        override fun compareTo(other: Cell): Int {
            return totalCost - other.totalCost
        }

        fun calcHeuristicCost(destCell: Cell) : Int {
            return square(row - destCell.row) + square(col - destCell.col)
        }
    }

    private companion object {
        val dirMatrix = arrayOf(
            Vector2(0, 1),
            Vector2(0, -1),
            Vector2(1, 0),
            Vector2(-1, 0),
            Vector2(1, 1),
            Vector2(1, -1),
            Vector2(-1, 1),
            Vector2(-1, -1)
        )
    }

    var grid: Array<Array<Cell>> = newGrid()
    private val queue = PriorityQueue<Cell>()

    private fun isDestination(c: Cell, destCell: Cell): Boolean {
        return c.row == destCell.row && c.col == destCell.col
    }

    private fun newGrid(): Array<Array<Cell>> {
        return Array(gridHeight) { row ->
            Array(gridWidth) { col -> Cell(row = row, col = col) }
        }
    }

    private fun tracePath(end: Cell): Array<Vector2<Int>> {
        val path = mutableSetOf<Vector2<Int>>()
        var row = end.row
        var col = end.col

        while (!(grid[row][col].parentRow == row && grid[row][col].parentCol == col)) {
            path.add(Vector2(grid[row][col].row, grid[row][col].col))
            val tRow = grid[row][col].parentRow
            val tCol = grid[row][col].parentCol

            row = tRow ?: throw RuntimeException("tracePath encountered a null parentRow")
            col = tCol ?: throw RuntimeException("tracePath encountered a null parentCol")
        }

        path.add(Vector2(grid[row][col].row, grid[row][col].col))
        grid = newGrid() // Let the old mutated grid be collected by GC

        return path.reversed().toTypedArray()
    }

    fun buildPath(start: Vector2<Int>, end: Vector2<Int>): Array<Vector2<Int>> {

        if (start.x !in 0..<gridWidth || start.y !in 0..<gridHeight
            || end.x !in 0..<gridWidth || end.y !in 0..<gridHeight) {

            throw RuntimeException("Start or End is Invalid")
        }

        val start = grid[start.x][start.y]
        val end = grid[end.x][end.y]

        if (end.blocked || start.blocked) {
            throw RuntimeException("Start or End is Blocked")
        }

        if (isDestination(start, end)) {
            return emptyArray()
        }

        start.distanceCost = 0
        start.parentRow = start.row
        start.parentCol = start.col

        queue.add(start)

        while (queue.isNotEmpty()) {
            val cell = queue.poll() ?: throw RuntimeException("Priority Queue Returned Null")

            cell.visited = true

            for (dir in dirMatrix) {
                val row = cell.row + dir.x
                val col = cell.col + dir.y

                if (row !in 0..<gridWidth || col !in 0..<gridHeight) {
                    continue
                }

                val newCell = grid[cell.row + dir.x][cell.col + dir.y]

                if (!newCell.blocked && !newCell.visited) {
                    if (isDestination(newCell, end)) {
                        newCell.parentRow = cell.row
                        newCell.parentCol = cell.col

                        return tracePath(end)
                    } else {
                        if (newCell.totalCost > newCell.calcHeuristicCost(end) + newCell.distanceCost + 1 ||
                            newCell.totalCost == Cell.INF) {

                            newCell.heuristicCost = newCell.calcHeuristicCost(end)
                            newCell.distanceCost += 1
                            newCell.parentRow = cell.row
                            newCell.parentCol = cell.col

                            queue.add(newCell)
                        }
                    }
                }
            }
        }
        throw RuntimeException("Destination not found after searching")
    }
}

private fun square(x: Int): Int {
    return x * x
}
