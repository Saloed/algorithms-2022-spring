import kotlin.math.abs

class Board(private val numbers: Array<IntArray>) {
    private var metric = 0 // the number of elements is out of place
    private var zeroCoordinates = Pair(-1, -1) // zero is an empty cell

    init {
        for (i in numbers.indices) {
            for (j in numbers[i].indices) {
                val needNumber = i * numbersSize() + j + 1
                if (numbers[i][j] != needNumber && numbers[i][j] != 0) metric +=
                    abs(numbers[i][j] - needNumber) / 4 + abs(numbers[i][j] - needNumber) % 4
                if (numbers[i][j] == 0) zeroCoordinates = Pair(i, j)
            }
        }
    }

    // for immutability
    fun numbers() = numbers
    fun metric() = metric
    fun zeroCoordinates() = zeroCoordinates

    private fun numbersSize() = numbers.size

    fun isNeedPosition() = metric == 0

    private fun findElement(i: Int, j: Int) = numbers[i][j]

    private fun findCoordinates(number: Int): Pair<Int, Int> {
        for (i in numbers.indices) for (j in numbers[i].indices)
            if (numbers[i][j] == number) return Pair(i, j)
        return Pair(-1, -1)
    }

    private fun turn(thisNumbers: Array<IntArray> = numbers, x1: Int, y1: Int, x2: Int, y2: Int): Board? {
        if (x2 < 0 || x2 > numbersSize() - 1 || y2 < 0 || y2 > numbersSize() - 1) return null
        val temp = thisNumbers[x2][y2]
        thisNumbers[x2][y2] = thisNumbers[x1][y1]
        thisNumbers[x1][y1] = temp
        return Board(thisNumbers)
    }

    fun neighbors(): List<Board> {
        val neigh = mutableListOf<Board>()
        turn(
            getNewNumbers(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first, zeroCoordinates.second + 1
        )?.let { neigh.add(it) }
        turn(
            getNewNumbers(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first, zeroCoordinates.second - 1
        )?.let { neigh.add(it) }
        turn(
            getNewNumbers(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first + 1, zeroCoordinates.second
        )?.let { neigh.add(it) }
        turn(
            getNewNumbers(),
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first - 1, zeroCoordinates.second
        )?.let { neigh.add(it) }
        return neigh
    }

    private fun getNewNumbers(): Array<IntArray> = deepCopy(numbers)

    private fun deepCopy(original: Array<IntArray>): Array<IntArray> {
        val result = Array(original.size) { intArrayOf() }
        for (i in original.indices) {
            result[i] = IntArray(original[i].size)
            for (j in original[i].indices) result[i][j] = original[i][j]
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null || other !is Board) return false

        val board: Board = other
        if (board.numbersSize() != numbersSize()) return false

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
                if (j != numbers[i].lastIndex) res += " "
            }
            if (i != numbers.lastIndex) res += "\n"
        }
        return res
    }

    override fun hashCode(): Int {
        var result = numbers.contentDeepHashCode()
        result = 31 * result + metric
        result = 31 * result + zeroCoordinates.hashCode()
        return result
    }
}
