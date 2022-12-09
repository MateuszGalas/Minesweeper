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

/*    fun random(): Field {
        val list = listOf<Field>(Field.MINE, Field.BLANK, Field.BLANK, Field.BLANK)
        return list[Random.nextInt(4)]
    }*/

    fun printBoard() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                when(board[i][j]) {
                    Field.BLANK -> print(Field.BLANK.value)
                    Field.MINE -> print(Field.MINE.value)
                }
            }
            println()
        }
    }
}

fun main() {
    val mine = MineSweeper()
    mine.printBoard()
}
