package solver

import core.*
import solver.strategies.*

class HumanAI : AbstractPlayer() {

    lateinit var playerMap: PlayerMapFactory
    var isFirstMove = true


    var globalStrategy: AbstractGlobalStrategy? = null


    override fun getNextMove(): Move {
        if (isFirstMove) {
            playerMap = PlayerMapFactory(startLocation)
            isFirstMove = false
        }
        return globalStrategy?.nextMove() ?: let {
            globalStrategy = FindAllWormholes(playerMap)
            globalStrategy!!.nextMove()
        }
    }


    override fun setMoveResult(result: MoveResult) {
        globalStrategy!!.setMoveResults(result)
    }
}
