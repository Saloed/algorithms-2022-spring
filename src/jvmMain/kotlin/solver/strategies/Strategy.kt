package solver.strategies

import core.Direction
import core.Move
import solver.PlayerMapFactory


data class StrategyMove(val move: Move, val direction: Direction)

interface Strategy {
    fun nextMove(): StrategyMove
}


abstract class GlobalStrategy(private val playerMap: PlayerMapFactory) {
    var subStrategy: Strategy? = null

    fun nextMove(): Move {
        val move = subStrategy?.nextMove() ?: let {
            subStrategy = RyoikiTenkai(playerMap.currentMap)
            subStrategy!!.nextMove()
        }
        playerMap.lastDirection = move.direction
        return move.move
    }
    fun isActive(): Boolean = true

}
