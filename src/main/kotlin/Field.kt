import kotlin.random.Random

data class Cell(val x: Int, val y: Int)

object Field {

    private const val SIZE = 9
    private val gameField = Array(SIZE) { CharArray(SIZE) { '.' } }
    val playerField = Array(SIZE) { CharArray(SIZE) { '.' } }
    private val generatedCells = mutableListOf<Cell>()
    private val markedCells = mutableListOf<Cell>()
    private val unexploredCells = mutableListOf<Cell>()

    private const val MINE = 'X'
    private const val MARKED = '*'
    private const val FREE = '/'
    private const val UNEXPLORED = '.'

    private var GAME_OVER = false
    var FILLED = false
    var reservedCell = Cell(0, 0)

    fun fill(mines: Int) {
        var count = mines
        while (count > 0) {
            val x = Random.nextInt(0, 9)
            val y = Random.nextInt(0, 9)
            val cell = Cell(x, y)
            if (cell !in generatedCells && cell != reservedCell) {
                generatedCells += cell
                count--
            }
        }

        generatedCells.forEach { cell: Cell -> gameField[cell.y][cell.x] = 'X' }
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (gameField[i][j] == '.') {
                    var countMine = 0
                    countMine += countMine(j - 1, i - 1)
                    countMine += countMine(j, i - 1)
                    countMine += countMine(j + 1, i - 1)
                    countMine += countMine(j - 1, i)
                    countMine += countMine(j + 1, i)
                    countMine += countMine(j - 1, i + 1)
                    countMine += countMine(j, i + 1)
                    countMine += countMine(j + 1, i + 1)

                    if (countMine > 0) {
                        gameField[i][j] = countMine.toString().first()
                    }
                }
                unexploredCells.add(Cell(j, i))
            }
        }
        FILLED = true
    }

    fun mine(cell: Cell) {
        if (playerField[cell.y][cell.x] == FREE) {
            return
        }

        if (playerField[cell.y][cell.x].isDigit()) {
            println("There is a number here!")
            return
        }

        playerField[cell.y][cell.x] = if (playerField[cell.y][cell.x] == UNEXPLORED) {
            markedCells.add(cell)
            unexploredCells.remove(cell)
            MARKED
        } else {
            markedCells.remove(cell)
            unexploredCells.add(cell)
            UNEXPLORED
        }

        if (markedCells.containsAll(generatedCells) && markedCells.size == generatedCells.size) {
            printField(playerField)
            println("Congratulations! You found all the mines!")
            GAME_OVER = true
        }
    }

    fun free(cell: Cell) {
        if (!cellIsInField(cell)) {
            return
        }

        if (gameField[cell.y][cell.x] == MINE) {
            GAME_OVER = true
            playerField[cell.y][cell.x] = gameField[cell.y][cell.x]
            printField(playerField)
            println("You stepped on a mine and failed!")
            return
        }

        if (gameField[cell.y][cell.x].isDigit()) {
            playerField[cell.y][cell.x] = gameField[cell.y][cell.x]
            unexploredCells.remove(Cell(cell.x, cell.y))
            return
        }

        if (playerField[cell.y][cell.x] == FREE) {
            return
        }

        if (playerField[cell.y][cell.x] == MARKED) {
            markedCells.remove(Cell(cell.x, cell.y))
        }

        playerField[cell.y][cell.x] = FREE
        unexploredCells.remove(Cell(cell.x, cell.y))
        free(Cell(cell.x, cell.y - 1))
        free(Cell(cell.x, cell.y + 1))
        free(Cell(cell.x - 1, cell.y))
        free(Cell(cell.x + 1, cell.y))
        free(Cell(cell.x - 1, cell.y - 1))
        free(Cell(cell.x + 1, cell.y - 1))
        free(Cell(cell.x - 1, cell.y + 1))
        free(Cell(cell.x + 1, cell.y + 1))
    }

    fun printField(field: Array<CharArray>) {
        print(" |")
        for (i in 1..field.size) {
            print("$i")
        }
        println("|")
        print("-|")
        for (i in 1..field.size) {
            print("-")
        }
        println("|")
        for (i in field.indices) {
            print("${i + 1}|")
            for (element in field[i]) {
                print("$element")
            }
            println("|")
        }
        print("-|")
        for (i in 1..field.size) {
            print("-")
        }
        println("|")
    }

    fun isGameOver(): Boolean = GAME_OVER

    fun allExploredSafeCells() {
        if (unexploredCells.containsAll(generatedCells) && unexploredCells.size == generatedCells.size) {
            printField(playerField)
            println("Congratulations! You found all the mines!")
            GAME_OVER = true
        }
    }

    private fun cellIsInField(cell: Cell): Boolean = cell.x in 0 until SIZE && cell.y in 0 until SIZE

    private fun countMine(x: Int, y: Int): Int {
        if (cellIsInField(Cell(x, y))) {
            if (gameField[y][x] == 'X') {
                return 1
            }
        }
        return 0
    }
}

