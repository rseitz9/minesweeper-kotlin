import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameTests {
    @Test
    fun `throws error when size is less than 1 x 1`() {
        val exception: Exception = assertThrows { Game(0, 5, 1) }
        assertEquals(exception::class.java, IllegalArgumentException::class.java)
    }

    @Test
    fun `throws error when there are more mines than spaces`() {
        val exception: Exception = assertThrows { Game(1, 1, 2) }
        assertEquals(exception::class.java, IllegalArgumentException::class.java)
    }

    @Test
    fun `generates correct board size`() {
        val game = Game(5, 4, 1)
        assertEquals(5, game.gameMatrix.size)
        assertEquals(4, game.gameMatrix[0].size)
    }

    @Test
    fun `generates correct number of mines`() {
        val game = Game(5, 5, 9);
        var minesFound = 0
        game.gameMatrix.forEach { arrayOfCells ->
            arrayOfCells.forEach { cell ->
                if (cell.mine == true) {
                    minesFound++
                }
            }
        }
        assertEquals(9, minesFound)
    }

    @Test
    fun `should initialize cells with correct numbers`() {
        val game = Game(3, 3, 0);
        game.setMine(0, 0)
        game.setMine(0, 1)
        game.setMine(0, 2)

        game.initializeCellNumbers()

        assertEquals(2, game.getCell(1, 0).number)
        assertEquals(3, game.getCell(1, 1).number)
        assertEquals(2, game.getCell(1, 2).number)

        assertEquals(0, game.getCell(2, 0).number)
        assertEquals(0, game.getCell(2, 1).number)
        assertEquals(0, game.getCell(2, 2).number)
    }

    @Test
    fun `should reveal all neighboring 0 cells`() {
        val game = Game(3, 3, 0);
        game.setMine(0, 0)
        game.initializeCellNumbers()

        game.revealCell(2, 2)

        assertFalse { game.gameMatrix[0][0].revealed }
        assertTrue { game.gameMatrix[0][1].revealed }
        assertTrue { game.gameMatrix[0][2].revealed }

        assertTrue { game.gameMatrix[1][0].revealed }
        assertTrue { game.gameMatrix[1][1].revealed }
        assertTrue { game.gameMatrix[1][2].revealed }

        assertTrue { game.gameMatrix[2][0].revealed }
        assertTrue { game.gameMatrix[2][1].revealed }
        assertTrue { game.gameMatrix[2][2].revealed }
    }

    @Test
    fun `should trigger game won when all cells are revealed`() {
        val game = Game(3, 3, 0);
        game.initializeCellNumbers()

        game.revealCell(1, 1)

        assertTrue { game.gameWon() }
        assertFalse { game.gameLost() }
    }

    @Test
    fun `should trigger game lost when a mine is revealed`() {
        val game = Game(3, 3, 1);
        game.setMine(2, 2)
        game.initializeCellNumbers()

        game.revealCell(2, 2)

        assertFalse { game.gameWon() }
        assertTrue { game.gameLost() }
    }

}
