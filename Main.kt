package minesweeper

import kotlin.random.Random

const val BoardSize = 9

enum class Field( val value: String) {
    MINE( "."),
    NUMBER("."),
    BLANK("."),
    MARKED_BLANK("*"),
    MARKED_MINE("*"),
    MARKED_NUMBER("*"),
    FREE("/"),
    FREE_NUMBER("."),
    FREE_MINE("X")
}

class MineSweeper {
    private val board = Array(BoardSize) { Array(BoardSize) { Field.BLANK } }
    private val boardOfNumbers = Array(BoardSize) { Array(BoardSize) { 0 } }
    private var startGame = true

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
        initializeBoardOfNumbers()
    }

    private fun initializeBoardOfNumbers() {
        for (i in board.indices) {
            for (j in board.indices) {
                if (board[i][j] == Field.BLANK) {
                    boardOfNumbers[i][j] = getAdjacent(i, j).count { it == Field.MINE }

                    if (boardOfNumbers[i][j] != 0) {
                        board[i][j] = Field.NUMBER
                    }
                } else if (board[i][j] == Field.NUMBER) {
                    boardOfNumbers[i][j] = getAdjacent(i, j).count { it == Field.MINE }

                    if (boardOfNumbers[i][j] != 0) {
                        board[i][j] = Field.NUMBER
                    } else {
                        board[i][j] = Field.BLANK
                    }
                }
            }
        }
    }

    private fun reassignMine(x: Int, y: Int) {
        val randomCell = Random.nextInt(BoardSize)
        val randomCell2 = Random.nextInt(BoardSize)

        if (board[randomCell][randomCell2] == Field.BLANK ||
            board[randomCell][randomCell2] == Field.NUMBER
            && x != randomCell
        ) {
            board[randomCell][randomCell2] = Field.MINE
        } else {
            reassignMine(x, y)
        }
    }

    private fun exploreCell(x: Int, y: Int) {
        if (startGame) {
            if (board[x][y] == Field.MINE) {
                if (getAdjacent(x, y).count { it == Field.MINE } == 0) {
                    board[x][y] = Field.BLANK
                } else {
                    boardOfNumbers[x][y] = getAdjacent(x, y).count { it == Field.MINE }
                    board[x][y] = Field.NUMBER
                }
                reassignMine(x, y)
                initializeBoardOfNumbers()
            }
            startGame = false
            return exploreCell(x, y)
        }

        if (board[x][y] == Field.BLANK) {
            val n = board.lastIndex
            val m = board[0].lastIndex

            if (getAdjacent(x, y).count { it == Field.MINE } == 0) {
                board[x][y] = Field.FREE
                for (i in (if (x > 0) -1 else 0)..if (x < n) 1 else 0) {
                    for (j in (if (y > 0) -1 else 0)..if (y < m) 1 else 0) {
                        if (i != 0 || j != 0) {
                            exploreCell(x + i, y + j)
                        }
                    }
                }
            }
        } else if (board[x][y] == Field.MARKED_BLANK) {
            board[x][y] = Field.BLANK
            exploreCell(x, y)
        } else if (board[x][y] == Field.MARKED_NUMBER) {
            board[x][y] = Field.FREE_NUMBER
        } else if (board[x][y] == Field.NUMBER) {
            board[x][y] = Field.FREE_NUMBER
        } else if (board[x][y] == Field.MINE) {
            for (i in board.indices) {
                for (j in board.indices) {
                    if (board[i][j] == Field.MINE) board[i][j] = Field.FREE_MINE
                }
            }
        }
    }

    private fun setUnsetMarkOnMine(x: Int, y: Int) {
        when (board[x][y]) {
            Field.BLANK -> board[x][y] = Field.MARKED_BLANK
            Field.MINE -> board[x][y] = Field.MARKED_MINE
            Field.MARKED_MINE -> board[x][y] = Field.MINE
            Field.MARKED_BLANK -> board[x][y] = Field.BLANK
            Field.NUMBER -> board[x][y] = Field.MARKED_NUMBER
            Field.MARKED_NUMBER -> board[x][y] = Field.NUMBER
            else -> println("There is a number here!")
        }
    }

    private fun printBoard() {
        print(" |")
        repeat(BoardSize) { print(it + 1) }
        println("|")
        print("-|")
        print("-".repeat(BoardSize))
        println("|")

        for (i in board.indices) {
            print("${i + 1}|")
            for (j in board[i].indices) {
                when (board[i][j]) {
                    Field.BLANK -> print(Field.BLANK.value)
                    Field.MINE -> print(Field.MINE.value)
                    Field.NUMBER -> print(Field.NUMBER.value)
                    Field.FREE -> print(Field.FREE.value)
                    Field.FREE_NUMBER -> print(boardOfNumbers[i][j])
                    Field.FREE_MINE -> print(Field.FREE_MINE.value)
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

    private fun checkIfSteppedOnMine(): Boolean {
        for (i in board) {
            for (j in i) {
                if (j == Field.FREE_MINE) {
                    printBoard()
                    println("You stepped on a mine and failed!")
                    return true
                }
            }
        }
        return false
    }

    private fun checkResult(): Boolean {
        for (i in board) {
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

    private fun checkBlankResult(): Boolean {
        for (i in board) {
            for (j in i) {
                if (j == Field.BLANK || j == Field.NUMBER) {
                    return false
                }
            }
        }
        return true
    }

    fun play() {
        printBoard()

        while (true) {
            print("Set/unset mines marks or claim a cell as free: ")
            val coordinates = readln().split(" ")
            val x = coordinates[0].toInt() - 1
            val y = coordinates[1].toInt() - 1
            val choice = coordinates[2]
            if (choice == "free") {
                exploreCell(y, x)
            } else {
                setUnsetMarkOnMine(y, x)
            }

            if (checkIfSteppedOnMine()) break
            if (checkResult() || checkBlankResult()) {
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

