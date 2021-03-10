class Cell(val row: Int, val column: Int, var mine: Boolean = false, var revealed: Boolean = false, var number: Int = 0) {

    fun initializeCellNumber(neighbors: List<Cell>) {
        val sum = neighbors.map { n -> if (n.mine == true) 1 else 0 }.sum()
        this.number = sum
    }
}
