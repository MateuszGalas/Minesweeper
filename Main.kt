package minesweeper

import kotlin.random.Random

const val BoardSize = 9

enum class Field(val hidden: String, val value: String) {
    MINE("hidden", "."),
    NUMBER("", ""),
    BLANK("hidden", "."),
    MARKED_BLANK("hidden", "*"),
    MARKED_MINE("hidden", "*")
}

class MineSweeper {
    private val board = Array(BoardSize) { Array(BoardSize) { Field.BLANK } }
    private val boardOfNumbers = Array(BoardSize) { Array(BoardSize) { 0 } }

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

        for (i in board.indices) {
            for (j in board.indices) {
                boardOfNumbers[i][j] = getAdjacent(i, j).count { it == Field.MINE }

                if (boardOfNumbers[i][j] != 0) {
                    board[i][j] = Field.NUMBER
                }
            }
        }
    }

    fun setUnsetMarkOnMine(x: Int, y: Int) {
        when (board[x][y]){
            Field.BLANK -> board[x][y] = Field.MARKED_BLANK
            Field.MINE -> board[x][y] = Field.MARKED_MINE
            Field.MARKED_MINE -> board[x][y] = Field.MINE
            Field.MARKED_BLANK -> board[x][y] = Field.BLANK
            else -> println("There is a number here!")
        }
    }

    fun printBoard() {
        print(" |")
        repeat(BoardSize) { print(it + 1) }
        println("|")
        print("-|")
        print("-".repeat(BoardSize))
        println("|")

        for (i in board.indices) {
            print("${i + 1}|")
            for (j in board[i].indices) {
                when(board[i][j]) {
                    Field.BLANK -> print(Field.BLANK.value)
                    Field.MINE -> print(Field.MINE.value)
                    Field.NUMBER -> print(boardOfNumbers[i][j])
                    else -> print(Field.MARKED_MINE.value)
                }
            }
            println("|")
        }
        print("-|")
        print("-".repeat(BoardSize))
        println("|")
    }

    private fun getAdjacent(x: Int, y: Int): ArrayList<Field> {
        // Size of given 2d array
        val n = board.lastIndex
        val m = board[0].lastIndex

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

    fun checkResult(): Boolean {
        for (i in board){
            for (j in i) {
                if (j == Field.MINE) {
                    return false
                } else if (j == Field.MARKED_BLANK) {
                    return false
                }
            }
        }
        return true
    }

    fun play() {
        printBoard()

        while (true){
            print("Set/delete mines marks (x and y coordinates): ")
            val coordinates = readln().split(" ")
            val x = coordinates[0].toInt() - 1
            val y = coordinates[1].toInt() - 1
            setUnsetMarkOnMine(y, x)

            if (checkResult()) {
                printBoard()
                println("Congratulations! You found all the mines!")
                break
            }
            printBoard()
        }
    }
}

fun main() {
    val mine = MineSweeper()
    mine.play()
}

