package solver.strategies

import core.Move
import core.MoveResult
import solver.PlayerMapFactory

abstract class AbstractGlobalStrategy(private val playerMap: PlayerMapFactory) {
    var subStrategy: Strategy? = null

    abstract fun getNextMove(): StrategyMove

    fun nextMove(): Move {
        val move = getNextMove()
        playerMap.lastDirection = move.direction
        return move.move
    }
    fun isActive(): Boolean = true

    abstract fun setResults()

    fun setMoveResults(result: MoveResult) {
        setResults()
        playerMap.updateCurrentMap(result)
    }

}