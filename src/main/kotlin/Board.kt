class Board(private val numbers: Array<IntArray>) {
    private var metric = 0 // the number of elements is out of place
    private var zeroCoordinates = Pair(-1, -1) // zero is an empty cell

    init {
        for (i in numbers.indices) {
            for (j in numbers[i].indices) {
                if (numbers[i][j] != i * numbersSize() + j + 1 && numbers[i][j] != 0) metric++
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
        val temp = thisNumbers[x1][y1]
        thisNumbers[x1][y1] = thisNumbers[x2][y2]
        thisNumbers[x2][y2] = temp
        return Board(thisNumbers)
    }

    fun neighbors(): MutableList<Board> {
        val neigh = mutableListOf<Board>()
        val thisNumbers = numbers
        turn(
            thisNumbers,
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first, zeroCoordinates.second + 1
        )?.let { neigh.add(it) }
        turn(
            thisNumbers,
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first, zeroCoordinates.second - 1
        )?.let { neigh.add(it) }
        turn(
            thisNumbers,
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first + 1, zeroCoordinates.second
        )?.let { neigh.add(it) }
        turn(
            thisNumbers,
            zeroCoordinates.first, zeroCoordinates.second,
            zeroCoordinates.first - 1, zeroCoordinates.second
        )?.let { neigh.add(it) }
        return neigh
    }

//    override fun equals(other: Any?): Boolean {
//        if (other == this) return true
//        if (other == null || other !is Board) return false
//        if (other.numbersSize() != numbersSize()) return false
//
//        for (i in numbers.indices) for (j in numbers[i].indices)
//            if (numbers[i][j] != other.numbers[i][j]) return false
//
//        return true
//    }
//
    override fun toString(): String {
        var res = ""
        for (i in numbers.indices) {
            for (j in numbers[i].indices) {
                res += "${ numbers[i][j] } "
            }
            res += "\n"
        }
        return res
    }
//
//    override fun hashCode(): Int {
//        var result = numbers.contentDeepHashCode()
//        result = 31 * result + metric
//        result = 31 * result + zeroCoordinates.hashCode()
//        return result
//    }
}
