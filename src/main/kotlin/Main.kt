import Field.FILLED
import Field.isGameOver
import Field.playerField
import Field.printField
import Field.reservedCell
import Field.mine
import Field.free
import Field.allExploredSafeCells
import Field.fill


fun main() {
    print("How many mines do you want on the field? ")
    val mines = readLine()!!.toInt()

    printField(playerField)
    while (!isGameOver()) {
        print("Set/unset mines marks or claim a cell as free: ")
        val positions = readLine()!!.split(" ")
        val x = positions[0].toInt() - 1
        val y = positions[1].toInt() - 1
        when (positions[2]) {
            "free" -> {
                if (!FILLED) {
                    reservedCell = Cell(x, y)
                    fill(mines)
                }
                free(Cell(x, y))
                allExploredSafeCells()
            }
            "mine" -> {
                mine(Cell(x, y))
            }
        }
        if (!isGameOver()) {
            printField(playerField)
        }
    }
}