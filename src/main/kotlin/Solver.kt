import java.util.PriorityQueue

class Solver(private val initial: Board) {
    private val winTrace: MutableList<Board> = mutableListOf()

    private data class Node(
        val prevNode: Node?,
        val board: Board
    )

    init {
        solve()
    }

    private fun solve() {
        val unsolvablePosition = Board(
            arrayOf(
                intArrayOf(1, 2, 3, 4),
                intArrayOf(5, 6, 7, 8),
                intArrayOf(9, 10, 11, 12),
                intArrayOf(13, 15, 14, 0)
            )
        )
        if (initial.isNeedPosition() || initial == unsolvablePosition) saveResults(Node(null, initial))
        else {
            val priorityQueue = PriorityQueue<Node>(compareBy { it.board.metric() })

            val setWithAllBoards = mutableSetOf<Board>()
            val setWithPriorityQueue = mutableSetOf<Board>()

            priorityQueue.add(Node(null, initial))

            while (true) {
                val thisNode = priorityQueue.poll()
                setWithAllBoards.add(thisNode.board)
                priorityQueue.remove(thisNode)

                for (thisBoard in thisNode.board.neighborsWithMove()) {
                    if (thisBoard in setWithAllBoards || thisBoard in setWithPriorityQueue) continue

                    if (thisBoard.isNeedPosition() || thisBoard == unsolvablePosition) {
                        saveResults(Node(thisNode, thisBoard))
                        return
                    }

                    priorityQueue.add(Node(thisNode, thisBoard))
                    setWithPriorityQueue.add(thisBoard)
                }

            }
        }
    }

    private fun containsInPath(node: Node, board: Board): Boolean {
        var nowNode: Node? = node
        while (true) {
            if ((nowNode?.board ?: return false) == board) return true
            nowNode = nowNode.prevNode
        }
    }

    private fun metricWithDeep(node: Node): Int {
        var nowNode: Node? = node
        var count = 0
        val metric = node.board.metric()
        while (true) {
            count++
            nowNode = nowNode?.prevNode ?: return count + metric
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
    fun solution(): List<Board> = winTrace
}