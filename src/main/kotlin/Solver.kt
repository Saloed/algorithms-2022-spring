import java.util.PriorityQueue

class Solver(private val initial: Board) {

    private data class Node(
        val board: Board,
        val movies: MutableList<Int>
    )

    fun solve(): Pair<Board, List<Int>> {
        val unsolvablePosition = Board(
            arrayOf(
                intArrayOf(1, 2, 3, 4),
                intArrayOf(5, 6, 7, 8),
                intArrayOf(9, 10, 11, 12),
                intArrayOf(13, 15, 14, 0)
            )
        )
        if (initial.isNeedPosition() || initial == unsolvablePosition) return Pair(initial, listOf())

        val priorityQueue = PriorityQueue<Node>(compareBy { it.board.metric() })
        val setWithAllBoards = mutableSetOf<Board>()
        val setWithPriorityQueue = mutableSetOf<Board>()

        priorityQueue.add(Node(initial, mutableListOf()))

        while (true) {
            val thisNode = priorityQueue.poll()
            setWithAllBoards.add(thisNode.board)
            priorityQueue.remove(thisNode)

            for (moveCoordinates in thisNode.board.neighborsWithOutMove()) {
                val newBoardArray = thisNode.board.numbersCopy()
                var newBoard = Board(newBoardArray)
                val movies = thisNode.movies.toMutableList()
                movies.add(newBoardArray[moveCoordinates.first][moveCoordinates.second])

                newBoardArray[newBoard.zeroCoordinates().first][newBoard.zeroCoordinates().second] =
                    newBoardArray[moveCoordinates.first][moveCoordinates.second]
                        .also {
                            newBoardArray[moveCoordinates.first][moveCoordinates.second] =
                                newBoardArray[newBoard.zeroCoordinates().first][newBoard.zeroCoordinates().second]
                        }
                newBoard = Board(newBoardArray)

                if (newBoard in setWithAllBoards || newBoard in setWithPriorityQueue) continue

                val newNode = Node(newBoard, movies)

                if (newBoard.isNeedPosition() || newBoard == unsolvablePosition)
                    return Pair(newNode.board, newNode.movies)

                priorityQueue.add(newNode)
                setWithPriorityQueue.add(newBoard)
            }
        }


    }

    /*
    private fun containsInPath(node: Node, board: Board): Boolean {
        var nowNode: Node? = node
        while (true) {
            if ((nowNode?.board ?: return false) == board) return true
            nowNode = nowNode.prevNode
        }
    }

    private fun saveResults(node: Node) {
        var nowNode: Node? = node
        while (true) {
            winTrace.add(nowNode?.board ?: return winTrace.reverse())
            nowNode = nowNode.prevNode
        }
    }

    fun countMoves(): Int = winTrace.size - 1
    fun solution(): List<Int> = solve().second
    */
}