package minesweeper

import kotlin.random.Random

const val BoardSize = 9

enum class Field(val hidden: String, val value: String) {
    MINE("hidden", "X"),
    BLANK("hidden", ".")
}

class MineSweeper {
    private val board = Array(BoardSize) { Array<Field>(BoardSize) { Field.BLANK } }

    init {
        println("How many mines do you want on the field?")
        val numberOfMines = readln().toInt()
        var count = 0

        while (count < numberOfMines) {
            val randomCell = Random.nextInt(BoardSize)
            val randomCell2 = Random.nextInt(BoardSize)

            if (board[randomCell][randomCell2] == Field.BLANK) {
                board[randomCell][randomCell2] = Field.MINE
            } else {
                count--
            }
            count++
        }
    }

    fun printBoard() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                val numberOfMinesAround = getAdjacent(i, j).count { it == Field.MINE }

                when(board[i][j]) {
                    Field.BLANK -> print(
                        if(numberOfMinesAround == 0) Field.BLANK.value
                        else numberOfMinesAround
                    )
                    Field.MINE -> print(Field.MINE.value)
                }
            }
            println()
        }
    }

    private fun getAdjacent(x: Int, y: Int): ArrayList<Field> {

        // Size of given 2d array
        val n = board.lastIndex
        val m = board[0].lastIndex

        //println("$n and $m")

        // Initialising a vector array where
        // adjacent elements will be stored
        val v = ArrayList<Field>()

        // Checking for adjacent elements
        // and adding them to array

        // Deviation of row that gets adjusted
        // according to the provided position
        for (i in (if (x > 0) -1 else 0)..if (x < n) 1 else 0) {

            // Deviation of the column that
            // gets adjusted according to
            // the provided position
            for (j in (if (y > 0) -1 else 0)..if (y < m) 1 else 0) {
                if (i != 0 || j != 0) {
                    v.add(board[x + i][y + j])
                }
            }
        }
        // Returning the vector array
        return v
    }
}

fun main() {
    val mine = MineSweeper()
    mine.printBoard()
}

