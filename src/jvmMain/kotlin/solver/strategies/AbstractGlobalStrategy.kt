package solver.strategies

import core.Move
import core.MoveResult
import core.WalkMove
import core.Wormhole
import solver.PlayerMapFactory

sealed class AbstractGlobalStrategy(private val playerMap: PlayerMapFactory) {
    lateinit var subStrategy: Strategy

    abstract fun getNextMove(): WalkMove

    fun nextMove(): Move {
        val move = getNextMove()
        playerMap.lastDirection = move.direction
        return move
    }
    fun isActive(): Boolean = true

    abstract fun setResults(result: MoveResult)

    fun setMoveResults(result: MoveResult) {
        setResults(result)
        playerMap.updateCurrentMap(result)
        if (result.room is Wormhole) setResults(result)
    }

}