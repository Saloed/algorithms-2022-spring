package solver

import core.*
import graphics.ViewModel

class PlayerMapFactory(val actualStartLocation: Location) {
    var playerMaps = mutableListOf<PlayerMap>()
    var currentMapIndex = -1
    val globalWormholes = mutableMapOf<Location, Wormhole>()
    var wormholesTotal = 0
    var treasureFound = false
    val currentMap: PlayerMap get() = playerMaps[currentMapIndex]


    lateinit var lastDirection: Direction

    init {
        addNewMap(Entrance, actualStartLocation)
    }

    private fun addMap() = addNewMap(Wormhole(id = wormholesTotal)).also { wormholesTotal++ }

    private fun addNewMap(startRoom: Room, startLocation: Location = Location(0,0)) {
        currentMapIndex++
        println("newMapAdded")
        playerMaps.add(PlayerMap(startLocation, startRoom == Entrance))
        currentMap.calcToDiscover()
        updateGraphics()

    }

    private fun updateGraphics() {
        currentMap.let {
            ViewModel.updatePlayerMap(
                index = currentMapIndex,
                toDiscover = it.toDiscover, // TODO()
                current = it.roomToVisualize
            )
        }
    }

    fun updateCurrentMap(result: MoveResult) {
        currentMap.setLastMoveResults(result, lastDirection)
        updateGraphics()

        currentMap.apply {
            when (result.room) {
                Entrance -> entrance = currentLocation
                Exit -> exit = currentLocation
                is WithContent -> {
                    treasureFound = true
                    treasure = currentLocation
                }
                is Wormhole -> {
                    wormholesTotal++
                        // todo(addWormhole)
                    addMap()
                } // TODO("WORMHOLE without id")
                else -> {} //do nothing
            }
        }
        currentMap.discoverCellsToVisualize = mutableSetOf()
    }

}