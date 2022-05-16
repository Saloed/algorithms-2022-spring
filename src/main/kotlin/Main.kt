fun main(args: Array<String>) {
    val array1: Array<IntArray> = arrayOf(intArrayOf(0, 1, 2, 3), intArrayOf(4, 5, 6, 7), intArrayOf(8, 9, 10, 11), intArrayOf(12, 13, 14, 15))
    val array2: Array<IntArray> = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 0))
    val array3: Array<IntArray> = arrayOf(intArrayOf(1, 2, 3, 0), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 4))
    val array4: Array<IntArray> = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 15, 14, 0))

    val board = Board(array3)
    println(board)
    println(board.metric())

    val solver = Solver(board)
    println(solver.isSolvable())
    println(solver.countMoves())
    println(solver.solution())
}