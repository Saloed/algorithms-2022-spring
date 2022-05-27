import kotlin.math.abs

class Board(private val numbers: Array<IntArray>) {
    private var metric = 0
    private var zeroCoordinates = Pair(-1, -1) // zero is an empty cell

    init {
        for (i in numbers.indices)
            for (j in numbers[i].indices) {
                val needNumber = i * columnsSize() + j + 1
                if (numbers[i][j] != needNumber && numbers[i][j] != 0) {
//                    metric++
                    metric += abs(numbers[i][j] - needNumber) / columnsSize() + abs(numbers[i][j] - needNumber) % rowSize()
                }
                if (numbers[i][j] == 0) zeroCoordinates = Pair(i, j)
            }
    }

    // for immutability
    fun numbers() = numbers
    fun metric() = metric
    fun zeroCoordinates() = zeroCoordinates

    private fun columnsSize() = numbers.size
    private fun rowSize() = numbers[0].size

    fun isNeedPosition() = metric == 0

    private fun turn(thisNumbers: Array<IntArray>, x1: Int, y1: Int, x2: Int, y2: Int): Board? {
        if (x2 < 0 || x2 > columnsSize() - 1 || y2 < 0 || y2 > columnsSize() - 1) return null
        thisNumbers[x1][y1] = thisNumbers[x2][y2].also { thisNumbers[x2][y2] = thisNumbers[x1][y1] }
        return Board(thisNumbers)
    }

    fun neighborsWithMove(): Set<Board> {
        val neigh = mutableSetOf<Board>()
        turn(
            numbersCopy(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first, zeroCoordinates.second + 1
        )?.let { neigh.add(it) }
        turn(
            numbersCopy(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first, zeroCoordinates.second - 1
        )?.let { neigh.add(it) }
        turn(
            numbersCopy(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first + 1, zeroCoordinates.second
        )?.let { neigh.add(it) }
        turn(
            numbersCopy(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first - 1, zeroCoordinates.second
        )?.let { neigh.add(it) }
        return neigh
    }

    fun neighborsWithOutMove(): Set<Pair<Int, Int>> {
        val neigh = mutableSetOf<Pair<Int, Int>>()
        if (zeroCoordinates.first - 1 in 0 until columnsSize())
            neigh.add(Pair(zeroCoordinates.first - 1, zeroCoordinates.second))
        if (zeroCoordinates.first + 1 in 0 until columnsSize())
            neigh.add(Pair(zeroCoordinates.first + 1, zeroCoordinates.second))
        if (zeroCoordinates.second - 1 in 0 until rowSize())
            neigh.add(Pair(zeroCoordinates.first, zeroCoordinates.second - 1))
        if (zeroCoordinates.second + 1 in 0 until rowSize())
            neigh.add(Pair(zeroCoordinates.first, zeroCoordinates.second + 1))
        return neigh
    }

    fun numbersCopy(): Array<IntArray> = Array(numbers.size) { numbers[it].copyOf() }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null || other !is Board) return false

        val board: Board = other
        if (board.columnsSize() != columnsSize()) return false
        if (board.rowSize() != rowSize()) return false

//        numbers.contentDeepEquals(board.numbers)
        for (i in numbers.indices) for (j in numbers[i].indices)
            if (numbers[i][j] != board.numbers[i][j]) return false

        return true
    }

    override fun toString(): String {
        var res = ""
        for (i in numbers.indices) {
            for (j in numbers[i].indices) {
                if (numbers[i][j] / 10 == 0) res += " "
                res += "${numbers[i][j]}"
                if (j != rowSize() - 1) res += " "
            }
            if (i != columnsSize() - 1) res += "\n"
        }
        return res
    }

    override fun hashCode(): Int {
        return numbers.contentDeepHashCode()
    }
}
