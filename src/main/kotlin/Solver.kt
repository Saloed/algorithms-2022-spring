import java.util.PriorityQueue

class Solver(private val initial: Board) {
    private val winTrace: MutableList<Board> = mutableListOf()

    private class Node(
        val prevNode: Node?,
        val board: Board
    )

    init {
        if (isSolvable()) {
            val priorityQueue = PriorityQueue<Node>(10) { node1, node2 ->
                metric(node1).compareTo(metric(node2))
            }

            priorityQueue.add(Node(null, initial))

            while (true) {
                val thisNode = priorityQueue.poll()
//                println(thisNode.board)
//                val lolBoard = thisNode.board
//                val lolMetric = metric(thisNode)
                val lolSize = priorityQueue.size

//                println(lolBoard)
//                println(lolMetric)
                println(lolSize)

                if (thisNode.board.isNeedPosition()) {
                    saveResults(Node(thisNode, thisNode.board))
                    break
                }

                for (thisBoard in thisNode.board.neighbors())
                    if (!containsInPath(thisNode, thisBoard) && !containsInQueue(priorityQueue, thisBoard))
                        priorityQueue.add(Node(thisNode, thisBoard))

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

    private fun containsInQueue(queue: PriorityQueue<Node>, board: Board): Boolean {
        for (node in queue) if (node.board == board) return true
        return false
    }

    private fun metric(node: Node): Int {
        var nowNode: Node? = node
        var count = 0
        val metric = node.board.metric()
        while (true) {
            count++
            nowNode = nowNode?.prevNode ?: return count + metric
        }
    }

    fun isSolvable(): Boolean {
        val array = initial.numbers()
        var count = 0
        var isContinue = false
        var prevDice = array[0][0]

        if (prevDice == 0) {
            prevDice = array[0][1]
            isContinue = true
        }
        for (i in array.indices)
            for (j in array[i].indices) {
                if (i == 0 && j == 0) continue
                if (isContinue) {
                    isContinue = false
                    continue
                }
                if (array[i][j] == 0) {
                    if (j + 1 <= array[i].lastIndex) {
                        if (prevDice > array[i][j + 1]) count++
                        prevDice = array[i][j + 1]
                    } else if (i + 1 <= array.lastIndex) {
                        if (prevDice > array[i + 1][0]) count++
                        prevDice = array[i + 1][0]
                    }
                    isContinue = true
                    continue
                }
                if (prevDice > array[i][j]) count++
                prevDice = array[i][j]
            }
        return (count + initial.zeroCoordinates().first + 1) % 2 == 0
    }

    private fun saveResults(node: Node) {
        var nowNode: Node? = node
        while (true) {
            nowNode = nowNode?.prevNode ?: return winTrace.reverse()
            winTrace.add(nowNode.board)
        }
    }

    fun countMoves(): Int = winTrace.size - 1
    fun solution(): List<Board> = winTrace
}