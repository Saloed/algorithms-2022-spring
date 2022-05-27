package solver.strategies

import solver.PlayerMapFactory


class FindAllWormholes(private val playerMap: PlayerMapFactory) : AbstractGlobalStrategy(playerMap) {

    var currentStrategy: Strategy? = null

    override fun getNextMove(): StrategyMove =
        subStrategy?.nextMove() ?: let {
            subStrategy = RyoikiTenkai(playerMap.currentMap)
            subStrategy!!.nextMove()

    }

    override fun setResults() {

    }


}

