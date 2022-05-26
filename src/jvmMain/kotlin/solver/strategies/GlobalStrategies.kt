package solver.strategies

import solver.PlayerMapFactory


class DomainExpansion(gameMap: PlayerMapFactory) : GlobalStrategy(gameMap) {

    var currentStrategy: Strategy? = null


}