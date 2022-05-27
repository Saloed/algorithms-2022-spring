package solver.strategies

import core.*
import solver.PlayerMap
import solver.directionSet
import solver.plus


class RyoikiTenkai(val currentMap: PlayerMap) : Strategy() {


    // returns null when unable to move from location
    private fun chooseWalkMove(location: Location = currentMap.currentLocation): WalkMove? {
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
        return resDirection?.let { WalkMove(it) }
    }

    override fun nextMove(): WalkMove? =
        (nextMoves?.removeLastOrNull()
            ?: chooseWalkMove())
            ?: if (currentMap.toDiscover.isNotEmpty()) {
                nextMoves = findClosestOrGoal(currentMap)
                nextMoves!!.removeLastOrNull()
            } else null
}




