import kotlin.test.Test
import kotlin.test.assertEquals

class MyTests {

    private val needPosition = Board(arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 0)))

    @Test
    fun randomTests() {
        val numbersList = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).shuffled()
        val numbersArray = Array(4) { IntArray(4) }

        for (i in numbersArray.indices) for (j in numbersArray[i].indices)
            numbersArray[i][j] = numbersList[i * numbersArray.size + j]


        val board = Board(numbersArray)
//        println(board)
        val solver = Solver(board)

        if (solver.isSolvable()) {
            println("isSolvable")
            assertEquals(needPosition.toString(), solver.solution().last().toString(), "isSolvable")
        }
        else {
            println("isNotSolvable")
            assertEquals("-1", solver.countMoves().toString(), "isNotSolvable")
        }
    }

    @Test
    fun test19() {
        val numbersArray: Array<IntArray> = arrayOf(intArrayOf(1, 2, 3, 0), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 4))

        val board = Board(numbersArray)
        val solver = Solver(board)

        if (solver.isSolvable()) {
            println("isSolvable")
//            println("metric = ${solver.solution().last().metric()}")
            println("moves: ${solver.countMoves()}")
            assertEquals(needPosition.toString(), solver.solution().last().toString(), "isSolvable")
        }
        else {
            println("isNotSolvable")
            assertEquals("-1", solver.countMoves().toString(), "isNotSolvable")
        }
    }

    @Test
    fun test2() {
        val numbersArray: Array<IntArray> = arrayOf(intArrayOf(1, 3, 5, 7), intArrayOf(9, 11, 13, 15), intArrayOf(0, 2, 4, 6), intArrayOf(8, 10, 12, 14))

        val board = Board(numbersArray)
        val solver = Solver(board)

        if (solver.isSolvable()) {
            println("isSolvable")
            assertEquals(needPosition.toString(), solver.solution().last().toString(), "isSolvable")
        }
        else {
            println("isNotSolvable")
            assertEquals("-1", solver.countMoves().toString(), "isNotSolvable")
        }
    }
}