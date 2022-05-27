package solver.strategies

import core.WalkMove
class CheckLocation(route: ArrayDeque<WalkMove>) : Strategy() {

    init {
        nextMoves = ArrayDeque<WalkMove>(route)
    }

    override fun nextMove(): WalkMove? = nextMoves!!.removeLastOrNull()

}

