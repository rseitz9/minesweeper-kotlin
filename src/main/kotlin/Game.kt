import java.lang.IllegalArgumentException
import java.util.*
import kotlin.random.Random

class Game(private val width: Int = 10, private val height: Int = 10, private val mines: Int = 10) {
    var gameMatrix: Array<Array<Cell>>
    private var mineRevealed = false
    private var cellsRevealed = 0

    init {
        if (width < 1 || height < 1) {
            throw IllegalArgumentException("Must have width and height of at least 1x1")
        }

        if (width * height < mines) {
            throw IllegalArgumentException("Too many mines")
        }

        this.gameMatrix = Array(width) { x ->
            Array(height) { y ->
                Cell(x, y)
            }
        }

        var i = mines

        while (i > 0) {
            val (x, y) = getRandomGridLocation()
            if (gameMatrix[x][y].mine == false) {
                setMine(x, y)
                i--
            }
        }

        initializeCellNumbers()
    }

    fun gameLost(): Boolean {
        return mineRevealed
    }

    fun gameWon(): Boolean {
        return (cellsRevealed == (height * width - mines)) && !mineRevealed
    }

    fun setMine(x: Int, y: Int) {
        this.gameMatrix[x][y].mine = true
    }

    fun initializeCellNumbers() {
        gameMatrix.forEachIndexed() { xIndex, arrayOfCells ->
            arrayOfCells.forEachIndexed { yIndex, cell ->
                cell.initializeCellNumber(getCellNeighbors(xIndex, yIndex))
            }
        }
    }

    fun getCellNeighbors(x: Int, y: Int): List<Cell> {
        val neighbors = mutableListOf<Cell>()
        for (checkX in maxOf(x - 1, 0)..minOf(x + 1, width - 1)) {
            for (checkY in maxOf(y - 1, 0)..minOf(y + 1, height - 1)) {
                if (!(checkX == x && checkY == y)) {
                    neighbors.add(gameMatrix[checkX][checkY])
                }
            }
        }
        return neighbors
    }

    fun getCell(x: Int, y: Int): Cell {
        return this.gameMatrix[x][y]
    }

    fun revealCell(x: Int, y: Int) {
        if (x > height - 1 || y > width - 1) {
            throw IndexOutOfBoundsException()
        }

        val cell = gameMatrix[x][y]

        if (cell.revealed == true) {
            return
        }

        if (cell.mine) {
            mineRevealed = true;
            cellsRevealed++
            return
        }

        val cellQueue: Queue<Cell> = LinkedList()
        cellQueue.add(cell)
        while (!cellQueue.isEmpty()) {
            val c = cellQueue.remove()
            if (c.revealed == false) {
                c.revealed = true
                cellsRevealed++
            }
            if (c.number == 0) {
                cellQueue.addAll(getCellNeighbors(c.row, c.column).filter { e -> e.revealed == false })
            }
        }
    }

    fun getRandomGridLocation(): Pair<Int, Int> {
        val x = Random.nextInt(0, width)
        val y = Random.nextInt(0, height)
        return Pair(x, y)
    }

    fun stringifyGameBoard(): String {
        var board = "| "
        gameMatrix.forEach { arrayOfCells ->
            arrayOfCells.forEach { cell ->
                if (cell.revealed == true) {
                    board += if (cell.mine) "X | " else cell.number.toString() + " | "
                } else {
                    board += "_ | "
                }
            }
            board += "\n| "
        }
        return board
    }
}
