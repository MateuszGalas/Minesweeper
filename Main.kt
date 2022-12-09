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
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j] = random()
            }
        }
    }

    fun random(): Field {
        val list = listOf<Field>(Field.MINE, Field.BLANK, Field.BLANK, Field.BLANK)
        return list[Random.nextInt(4)]
    }

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