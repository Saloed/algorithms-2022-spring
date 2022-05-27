package solver.strategies

import core.*
import solver.PlayerMap
import solver.directionSet
import solver.plus


class RyoikiTenkai(val currentMap: PlayerMap) : Strategy {

    var nextMoves: ArrayDeque<WalkMove>? = null


    // returns False when unable to move from location
    private fun analyzeWallOrKnown(location: Location = currentMap.currentLocation): Pair<Boolean, Direction?> {
        var resDirection: Direction? = null
        var maxK = -1

        for (direction in directionSet.shuffled()) {
            val moveLocation = direction + location
            if (moveLocation !in currentMap.knownLocations) {
                var currentK = -1

                for (x in -1..1) {
                    for (y in -1..1) {
                        if (!(x == 0 && y == 0)) {
                            val checkedLocation = moveLocation + Location(x, y)
                            currentK = when (currentMap.knownLocations[checkedLocation]) {
                                Empty -> currentK + 1
                                Entrance -> currentK + 100
                                Exit -> currentK + 1
                                Wall -> currentK + 1
                                is WithContent -> currentK + 1
                                is Wormhole -> currentK + 1
                                null -> currentK
                            }
                            if (checkedLocation in currentMap.toDiscover) currentK++
                        }
                    }
                }
                if (currentK > maxK) {
                    resDirection = direction; maxK = currentK
                }
            }
        }
        return Pair(maxK == -1, resDirection)
    }

    override fun nextMove(): StrategyMove {
//        lateinit var resDirection: Direction
        var resMove: WalkMove? = null

        if (nextMoves != null) resMove = nextMoves!!.removeFirstOrNull()
        if (resMove == null) {
            val result = analyzeWallOrKnown(currentMap.currentLocation).second
            if (result == null) {
                if (currentMap.toDiscover.isNotEmpty()) {
                    nextMoves = findClosestOrGoal(currentMap)
                    resMove = nextMoves!!.removeLast()
                } else {
                    if (currentMap.wormholes.isEmpty()) {
                        nextMoves = findClosestOrGoal(currentMap, currentMap.exit!!)
                        resMove = nextMoves!!.removeLast()
                    } else {
                        nextMoves = findClosestOrGoal(currentMap, currentMap.wormholes.toList().last().first)
                        resMove = nextMoves!!.removeLast()
                    }
                }
            } else {
                resMove = WalkMove(result)
            }
        }
        return StrategyMove(resMove, resMove.direction)
    }
}




