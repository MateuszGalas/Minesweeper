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
        val numberOfBlank = (BoardSize * BoardSize) - numberOfMines
        var random: Field
        var mine = 0
        var blank = 0


        for (i in board.indices) {
            var j = 0

            while (j < BoardSize) {
                random = random()
                if (random == Field.MINE && mine < numberOfMines) {
                    board[i][j] = random
                    mine += 1
                } else if (random == Field.BLANK && blank < numberOfBlank) {
                    board[i][j] = random
                    blank += 1
                } else {
                    j--
                }
                j++
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
