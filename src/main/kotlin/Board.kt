import kotlin.math.abs

class Board private constructor(
    private val numbers: Array<IntArray>,
    private var zeroCoordinates: Pair<Int, Int>
) {

    private fun columnsSize() = numbers.size
    private fun rowSize() = numbers[0].size

    val metric: Int by lazy {
        var metric = 0
        for (i in numbers.indices)
            for (j in numbers[i].indices) {
                val needNumber = i * columnsSize() + j + 1
                if (numbers[i][j] != needNumber && numbers[i][j] != 0) {
//                    metric++
                    metric += abs(numbers[i][j] - needNumber) / columnsSize() + abs(numbers[i][j] - needNumber) % rowSize()
                }
            }
        metric
    }

    // for immutability
    fun numbers() = numbers
    fun zeroCoordinates() = zeroCoordinates

    fun turn(thisNumbers: Array<IntArray>, x: Int, y: Int): Board? {
        if (x < 0 || x > columnsSize() - 1 || y < 0 || y > columnsSize() - 1) return null
        thisNumbers[zeroCoordinates.first][zeroCoordinates.second] =
            thisNumbers[x][y].also { thisNumbers[x][y] = thisNumbers[zeroCoordinates.first][zeroCoordinates.second] }
        zeroCoordinates = Pair(x, y)
        return createBoard(thisNumbers)
    }

    fun neighbors(): Set<Pair<Int, Int>> {
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

        return numbers.contentDeepEquals(board.numbers)
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

    override fun hashCode(): Int = numbers.contentDeepHashCode()

    companion object {

        fun createBoard(numbers: Array<IntArray>): Board {
            var zeroCoordinates = Pair(-1, -1)
            for (i in numbers.indices) for (j in numbers[i].indices)
                if (numbers[i][j] == 0) zeroCoordinates = Pair(i, j)
            return Board(numbers, zeroCoordinates)
        }

    }
}
