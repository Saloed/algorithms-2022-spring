import Board.Companion.createBoard
import kotlin.test.Test
import kotlin.test.assertEquals

class MyTests {

    private val solvablePosition = createBoard(
        arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12),
            intArrayOf(13, 14, 15, 0)
        )
    )
    private val unsolvablePosition = createBoard(
        arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12),
            intArrayOf(13, 15, 14, 0)
        )
    )

    @Test
    fun randomTests() {
        for (iterations in 0..30) {
            val inputNumbersList = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).shuffled()
            val inputNumbersArray = Array(4) { IntArray(4) }

            for (i in inputNumbersArray.indices) for (j in inputNumbersArray[i].indices)
                inputNumbersArray[i][j] = inputNumbersList[i * inputNumbersArray.size + j]

            val board = createBoard(inputNumbersArray)
//        println(board)
            val solver = Solver(board)
            val (lastState, winMovies) = solver.solve()

            val resultPosition =
                when (lastState) {
                    solvablePosition -> solvablePosition
                    unsolvablePosition -> unsolvablePosition
                    else -> null
                }

            assertEquals(resultPosition.toString(), lastState.toString(), "")
            println("moves: ${winMovies.size}")
        }
    }

    @Test
    fun testShort1() {
        val inputNumbers: Array<IntArray> = arrayOf(
            intArrayOf(1, 2, 3, 0),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12),
            intArrayOf(13, 14, 15, 4)
        )

        val board = createBoard(inputNumbers)
        val solver = Solver(board)
        val (lastState, winMovies) = solver.solve()

        assertEquals(solvablePosition.toString(), lastState.toString(), "isSolvable")
        println("moves: ${winMovies.size}")
    }

    @Test
    fun testLong1() {
        val numbersArray: Array<IntArray> = arrayOf(
            intArrayOf(1, 3, 5, 7),
            intArrayOf(9, 11, 13, 15),
            intArrayOf(0, 2, 4, 6),
            intArrayOf(8, 10, 12, 14)
        )

        val board = createBoard(numbersArray)
        val solver = Solver(board)
        val (lastState, winMovies) = solver.solve()

        assertEquals(unsolvablePosition.toString(), lastState.toString(), "isNotSolvable")
        println("moves: ${winMovies.size}")
    }

    @Test
    fun testLong2() {
        val numbersArray: Array<IntArray> = arrayOf(
            intArrayOf(6, 12, 8, 13),
            intArrayOf(10, 9, 5, 3),
            intArrayOf(4, 15, 2, 0),
            intArrayOf(1, 7, 14, 11)
        )

        val board = createBoard(numbersArray)
        val solver = Solver(board)
        val (lastState, winMovies) = solver.solve()

        assertEquals(unsolvablePosition.toString(), lastState.toString(), "isNotSolvable")
        println("moves: ${winMovies.size}")
    }

    @Test
    fun testLong3() {
        val numbersArray: Array<IntArray> = arrayOf(
            intArrayOf(11, 9, 12, 14),
            intArrayOf(0, 10, 7, 13),
            intArrayOf(2, 5, 8, 4),
            intArrayOf(15, 1, 3, 6)
        )

        val board = createBoard(numbersArray)
        val solver = Solver(board)
        val (lastState, winMovies) = solver.solve()

        assertEquals(unsolvablePosition.toString(), lastState.toString(), "isNotSolvable")
        println("moves: ${winMovies.size}")
    }
}