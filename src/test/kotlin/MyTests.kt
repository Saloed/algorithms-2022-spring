import kotlin.test.Test
import kotlin.test.assertEquals

class MyTests {

    private val needPosition = Board(
        arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12),
            intArrayOf(13, 14, 15, 0)
        )
    )
    private val unsolvablePosition = Board(
        arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12),
            intArrayOf(13, 15, 14, 0)
        )
    )

    @Test
    fun randomTest() {
        val inputNumbersList = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).shuffled()
        val inputNumbersArray = Array(4) { IntArray(4) }

        for (i in inputNumbersArray.indices) for (j in inputNumbersArray[i].indices)
            inputNumbersArray[i][j] = inputNumbersList[i * inputNumbersArray.size + j]

        val board = Board(inputNumbersArray)
//        println(board)
        val solver = Solver(board)

        val resultPosition =
            if (solver.solution().last() == needPosition) needPosition
            else if (solver.solution().last() == unsolvablePosition) unsolvablePosition
            else null

        assertEquals(resultPosition.toString(), solver.solution().last().toString(), "")
        println("moves: ${solver.countMoves()}")

    }

    @Test
    fun testShort1() {
        val inputNumbers: Array<IntArray> = arrayOf(
            intArrayOf(1, 2, 3, 0),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12),
            intArrayOf(13, 14, 15, 4)
        )

        val board = Board(inputNumbers)
        val solver = Solver(board)

        assertEquals(needPosition.toString(), solver.solution().last().toString(), "isSolvable")
        println("moves: ${solver.countMoves()}")
    }

    @Test
    fun testLong1() {
        val numbersArray: Array<IntArray> = arrayOf(
            intArrayOf(1, 3, 5, 7),
            intArrayOf(9, 11, 13, 15),
            intArrayOf(0, 2, 4, 6),
            intArrayOf(8, 10, 12, 14)
        )

        val board = Board(numbersArray)
        val solver = Solver(board)

        assertEquals(unsolvablePosition.toString(), solver.solution().last().toString(), "isNotSolvable")
        println("moves: ${solver.countMoves()}")
    }
}