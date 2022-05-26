package solver

import core.*
import core.Exit
import core.Wormhole


class PlayerMap(val spawnLocation: Location, isPrimary: Boolean) {

    var discoverCellsToVisualize = mutableSetOf<Location>()
    var roomToVisualize: Pair<Location, Room>

    var currentLocation = spawnLocation

    val knownLocations = mutableMapOf<Location, Room>()
    val wormholes = mutableMapOf<Location, Wormhole>()
    val toDiscover = mutableSetOf<Location>()
    var entrance: Location? = null
    var exit: Location? = null

    var treasure: Location? = null


    init {
        if (isPrimary) {
            knownLocations[spawnLocation] = Entrance
            entrance = spawnLocation
            roomToVisualize= Pair(spawnLocation, Entrance)
        } else {
            knownLocations[spawnLocation] = Wormhole(0) // todo(Add actual wormhole id)
            wormholes[spawnLocation] = Wormhole(0) // todo(Add actual wormhole id)
            roomToVisualize = Pair(currentLocation, Wormhole(0)) // todo(Add actual wormhole id)
        }
        calcToDiscover()
    }


    fun calcToDiscover() {
        for (direction in setOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH)) {
            val location = direction + currentLocation
            if (location !in toDiscover && knownLocations[location] == null) {
                toDiscover.add(location)
                discoverCellsToVisualize.add(location)
            }
        }
    }

    // todo(revisit for changes)
    fun setLastMoveResults(res: MoveResult, lastMove:Direction) {
        roomToVisualize = (lastMove + currentLocation to res.room)
        if (res.successful) {
            currentLocation = lastMove + currentLocation
            knownLocations[currentLocation] = res.room
        } else {
            knownLocations[lastMove + currentLocation] = res.room
        }
        if (currentLocation in toDiscover) toDiscover.remove(currentLocation)
        if (currentLocation in discoverCellsToVisualize) discoverCellsToVisualize.remove(currentLocation)
        calcToDiscover()

        when(res.room) {
            Entrance -> entrance = currentLocation
            Exit -> exit = currentLocation
            is WithContent -> treasure = currentLocation
            is Wormhole -> wormholes[currentLocation] = Wormhole(0)
            else -> {}
        }
    }
}

