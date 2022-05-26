package solver

import core.*
import graphics.ViewModel
import solver.strategies.*

class HumanAI : AbstractPlayer() {

    lateinit var playerMap: PlayerMapFactory
    var isFirstMove = true


    var currentStrategy: GlobalStrategy? = null


    override fun getNextMove(): Move {
        if (isFirstMove) {
            playerMap = PlayerMapFactory(startLocation)
            isFirstMove = false
        }
        return currentStrategy?.nextMove() ?: let {
            currentStrategy = DomainExpansion(playerMap)
            currentStrategy!!.nextMove()
        }
    }


    override fun setMoveResult(result: MoveResult) {
        playerMap.updateCurrentMap(result)
    }
}
