package solver.strategies

import core.Direction
import core.Move
import solver.PlayerMapFactory


data class StrategyMove(val move: Move, val direction: Direction)

interface Strategy {
    fun nextMove(): StrategyMove
}

