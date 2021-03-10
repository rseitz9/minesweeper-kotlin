import java.lang.IndexOutOfBoundsException
import java.lang.Integer.parseInt

val COORDINATE_STRING = "please enter a coordinate in the form of row,column"
val OUT_OF_BOUNDS_STRING = "please enter a coordinate within the range of the matrix"

fun main() {
    val currentGame = Game(10, 10, 10)
    println(COORDINATE_STRING)
    while (!currentGame.gameLost() && !currentGame.gameWon()) {
        val enteredString = readLine()
        if (enteredString.isNullOrEmpty()) {
            println(COORDINATE_STRING)
            continue;
        }
        val coordinates = enteredString.split(',')
        var x: Int
        var y: Int

        try {
            x = parseInt(coordinates[0])
            y = parseInt(coordinates[1])
        } catch (e: Exception) {
            println(COORDINATE_STRING)
            continue;
        }

        try {
            currentGame.revealCell(x, y);
        } catch (e: IndexOutOfBoundsException) {
            println(OUT_OF_BOUNDS_STRING)
        }
        print(currentGame.stringifyGameBoard())
    }

    if (currentGame.gameWon()) {
        println("You Won!")
    }
    if (currentGame.gameLost()) {
        println("You Lost :(")
    }
}




