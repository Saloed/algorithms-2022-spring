package solver.strategies

import core.Direction
import core.Move
import core.WalkMove


//data class StrategyMove(val move: WalkMove, val direction: Direction)

sealed class Strategy {

    var isWaitMove = false
    var nextMoves: ArrayDeque<WalkMove>? = null

    abstract fun nextMove(): WalkMove?
}

