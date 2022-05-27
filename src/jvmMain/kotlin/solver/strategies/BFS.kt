package solver.strategies

import core.*
import solver.*

/** turn mapFactory updateGraphics off to test */
//fun main() {
//    val mapFactory = PlayerMapFactory(Location(5,5))
////    mapFactory.updateCurrentMap()
//    val result = MoveResult(Empty, Condition(false), true, "moveAdded")
//    mapFactory.currentMap.setLastMoveResults(result, Direction.EAST)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.EAST)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.EAST)
//
//    mapFactory.currentMap.setLastMoveResults(result, Direction.NORTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.NORTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.NORTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.NORTH)
//
//    mapFactory.currentMap.setLastMoveResults(result, Direction.WEST)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.WEST)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.WEST)
//
//    mapFactory.currentMap.setLastMoveResults(result, Direction.SOUTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.SOUTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.SOUTH)
//
//    mapFactory.currentMap.setLastMoveResults(result, Direction.EAST)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.EAST)
//
//    mapFactory.currentMap.setLastMoveResults(result, Direction.NORTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.NORTH)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.WEST)
//    mapFactory.currentMap.setLastMoveResults(result, Direction.SOUTH)
//
//
//    val map = arrayListOf(
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//        arrayListOf(" ", " ", " ", " ", " ", " ", " ", " ", " ", " "),
//    )
//    var counter = 0
//    mapFactory.currentMap.knownLocations.forEach {
//        map[it.key.y][it.key.x] = "X"
//        counter++
//        println("location = ${it.key}, valur= ${it.value}")
//    }
//    mapFactory.currentMap.toDiscover.forEach {
//        map[it.y][it.x] = "?"
//    }
//    mapFactory.currentMap.knownLocations.forEach {
//        if (it.key == mapFactory.currentMap.currentLocation) map[it.key.y][it.key.x] = "O"
//    }
//    map.forEach { println(it) }
//
//    val findClos = findClosestOrGoal(mapFactory, Location(8,0))
//    findClos.forEach {
//        println(it.direction)
//    }
//}


fun findClosestOrGoal(currentMap: PlayerMap, goal: Location? = null): ArrayDeque<WalkMove>{
    val startLocation = currentMap.currentLocation

    val toExpand = ArrayDeque<Location>()
    val knownLocations = mutableMapOf(startLocation to 0)
    var isRouteFound = false
    lateinit var endLocation: Location

    fun expandUntilGoal(location: Location) {
        for (direction in directionSet) {
            val possibleLocation = direction + location
            if (possibleLocation.let {
                    it !in knownLocations && it !in toExpand
                            && (it in currentMap.knownLocations && currentMap.knownLocations[it] !is Wall
                            || (it in currentMap.toDiscover && goal == null)) // todo(Check for Maze)
                }
            ) {
                toExpand.addLast(possibleLocation)
            }
        }
        val newLocation = toExpand.removeFirst()
        var minSteps = Int.MAX_VALUE
        for (direction in directionSet) {
            minSteps = minOf((knownLocations[direction + newLocation] ?: Int.MAX_VALUE), minSteps)
        }
        knownLocations[newLocation] = minSteps + 1
        println("$newLocation ${newLocation in currentMap.toDiscover} ${newLocation in currentMap.toDiscover && goal == null}" )
        if (newLocation in currentMap.toDiscover && goal == null || newLocation == goal) {
            isRouteFound = true
            endLocation = newLocation
        } else {
            expandUntilGoal(newLocation)
        }
    }
    expandUntilGoal(startLocation)
    println("goal= $endLocation")
    println("currentLoc= ${currentMap.currentLocation}")
    val route = ArrayDeque<WalkMove>()
    reconstructFromMap(knownLocations, currentMap.currentLocation, endLocation, route)
    println(route.map { it.direction })
    return route

}


fun reconstructFromMap(
    locations: Map<Location, Int>,
    start: Location,
    end: Location,
    currentRoute: ArrayDeque<WalkMove>
) {
    var minStep = Int.MAX_VALUE
    lateinit var resDirection: Direction
    lateinit var resEndLocation: Location
    for (direction in directionSet) {
        val location = direction + end
        if ((locations[location] ?: Int.MAX_VALUE) <= minStep) {
            minStep = locations[location] ?: Int.MAX_VALUE
            resDirection = direction
            resEndLocation = direction + end
        }
    }
    currentRoute.addLast(WalkMove(resDirection.turnBack()))
    if (start != resEndLocation) reconstructFromMap(locations, start, resEndLocation, currentRoute)
}

