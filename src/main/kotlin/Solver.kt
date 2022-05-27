import Board.Companion.createBoard
import java.util.PriorityQueue

class Solver(private val initial: Board) {

    private data class Node(
        val board: Board,
        val moves: MutableList<Int>
    )

    fun solve(): Pair<Board, List<Int>> {
        if (isEndPosition(initial)) return Pair(initial, listOf())

        val priorityQueue = PriorityQueue<Node>(compareBy { it.board.metric })
        val setWithAllBoards = mutableSetOf<Board>()
        val setWithPriorityQueue = mutableSetOf<Board>()

        priorityQueue.add(Node(initial, mutableListOf()))

        while (true) {
            val thisNode = priorityQueue.poll()
            setWithAllBoards.add(thisNode.board)
            priorityQueue.remove(thisNode)

            for (moveCoordinates in thisNode.board.neighbors()) {
                val newBoardArray = thisNode.board.numbersCopy()
                val newBoard = createBoard(newBoardArray)
                val moves = thisNode.moves.toMutableList()
                moves.add(newBoardArray[moveCoordinates.first][moveCoordinates.second])

                newBoard.turn(newBoardArray, moveCoordinates.first, moveCoordinates.second)

                if (newBoard in setWithAllBoards || newBoard in setWithPriorityQueue) continue
                if (isEndPosition(newBoard)) return Pair(newBoard, moves)

                priorityQueue.add(Node(newBoard, moves))
                setWithPriorityQueue.add(newBoard)
            }

        }

    }

    private fun isEndPosition(board: Board): Boolean {
        val solvablePosition = createBoard(
            arrayOf(
                intArrayOf(1, 2, 3, 4),
                intArrayOf(5, 6, 7, 8),
                intArrayOf(9, 10, 11, 12),
                intArrayOf(13, 14, 15, 0)
            )
        )
        val unsolvablePosition = createBoard(
            arrayOf(
                intArrayOf(1, 2, 3, 4),
                intArrayOf(5, 6, 7, 8),
                intArrayOf(9, 10, 11, 12),
                intArrayOf(13, 15, 14, 0)
            )
        )
        return board == solvablePosition || board == unsolvablePosition
    }

}