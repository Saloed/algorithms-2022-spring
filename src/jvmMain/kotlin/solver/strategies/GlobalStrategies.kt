package solver.strategies

import core.*
import solver.PlayerMap
import solver.PlayerMapFactory


class FindAllWormholes(private val mapFactory: PlayerMapFactory) : AbstractGlobalStrategy(mapFactory) {

    init {
        subStrategy = RyoikiTenkai(mapFactory.currentMap)
    }

    private var checkRouteToStart: ArrayDeque<WalkMove>? = null
//    var allowCheckLocations = false
    private var checkRouteFromStart: ArrayDeque<WalkMove> = ArrayDeque()

    override fun getNextMove(): WalkMove =
        subStrategy.nextMove() ?: let {
            when (subStrategy) {
                is CheckLocation -> let {
                    subStrategy =
                        CheckLocation(findClosestOrGoal(mapFactory.currentMap, mapFactory.currentMap.spawnLocation))
                }
                is RyoikiTenkai -> subStrategy.apply {
                    nextMoves = findClosestOrGoal(
                        mapFactory.currentMap,
                        mapFactory.currentMap.exit
                    ) // if all locations in map are discovered
                }
            }
            subStrategy.nextMove()!!
        }

    private fun PlayerMap.recalculateLocationsAndMerge(room: Room) {
//        if (mapFactory.playerMaps[0] == this)
    }


    override fun setResults(result: MoveResult) {
        println(result.successful)
        if (result.successful) {
            if (mapFactory.currentMapIndex != 0) mapFactory.currentMap.recalculateLocationsAndMerge(result.room)
            when (result.room) {
                Entrance -> if (mapFactory.currentMapIndex != 0) {
                    when (subStrategy) {
                        is CheckLocation -> subStrategy = RyoikiTenkai(mapFactory.currentMap)
                        is RyoikiTenkai -> {} // DO nothing
                    }
                }
                Exit -> { }
                is WithContent -> {  }
                is Wormhole -> {
                    if (checkRouteToStart == null) {
                        checkRouteToStart = findClosestOrGoal(
                            currentMap = mapFactory.currentMap,
                            goal = mapFactory.currentMap.entrance,
                            start = mapFactory.lastDirection + mapFactory.currentMap.currentLocation
                        )
                        checkRouteFromStart = ArrayDeque(checkRouteToStart!!.reversed())
                    }
                    subStrategy = CheckLocation(checkRouteToStart!!)
                }
                else -> {}
            }
        } else {
            when (subStrategy) {
                is CheckLocation -> subStrategy =
                    CheckLocation(findClosestOrGoal(mapFactory.currentMap, mapFactory.currentMap.spawnLocation))
                is RyoikiTenkai -> {}
            }
        }
    }


}


