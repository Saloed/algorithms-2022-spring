package solver.strategies

import core.*
import solver.PlayerMap
import solver.directionSet
import solver.plus


class RyoikiTenkai(val currentMap: PlayerMap) : Strategy {

    // returns False when unable to move from location
    fun analyzeWallOrKnown(location: Location = currentMap.currentLocation): Pair<Boolean, Direction> {
        var resDirection: Direction? = null
        var maxK = -1

        for (direction in directionSet.shuffled()) {
            val moveLocation = direction + location
            if (moveLocation !in currentMap.knownLocations) {
//                println("for moveDirection: $direction, possibleMoveLocation = $moveLocation, known= ${currentMap.knownLocations[moveLocation] != null}")
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
//                            println("   for x=$x, y=$y, k=$currentK")
//                            println("   room at location=${currentMap.knownLocations[checkedLocation]}")
//                            println("   in toDiscover=${checkedLocation in currentMap.toDiscover}")

                        }
                    }
                }
                if (currentK > maxK) {
                    resDirection = direction; maxK = currentK
                }
            }
        }

        return Pair(maxK == -1, resDirection!!)
    }

    override fun nextMove(): StrategyMove {
        val strategyDirection = analyzeWallOrKnown(currentMap.currentLocation).second

        return StrategyMove(WalkMove(strategyDirection), strategyDirection)
    }
}




