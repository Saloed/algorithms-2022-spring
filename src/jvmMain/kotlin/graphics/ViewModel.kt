package graphics

import solver.HumanAI
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import core.*
import lab.Controller
import lab.Labyrinth
import solver.plus
import java.lang.Thread.sleep
import kotlin.concurrent.thread


// TODO(Make a game object on press start)
object ViewModel {
    var gameState = GameState()
    val isRunning = mutableStateOf(false)
    val mapsTotal = mutableStateOf(0)

    fun updatePlayerMap(
        index: Int,
        toDiscover: Set<Location> = setOf(),
        current: MutableMap<Location, Room>
    ) {
        current.forEach { (location, room) ->
            gameState.apply {
                val ch = getCharFromRoom(room)
                if (index + 1 > allMaps.size - 1) addEmptyMap()
                val offset = if (index == 0) Location(1, 1) else allMapsOffsets[index + 1]!!
                allMaps[index + 1][(location + offset).y][(location + offset).x].value = ch.toString()
                toDiscover.forEach {
                    allMaps[index + 1][it.y + offset.y][it.x + offset.x].value = "*"
                }
            }
        }
    }

    fun updateCurrentLocation(location: Location) {
        gameState.apply {
            if (currPos == null) startLoc = location
            currPos = location
            stateOfCurrPose.value = location
            prevPos = currPos
        }
    }
    fun removeLastMap() {
        gameState.apply {
            mapsTotal.value--
            allMaps.removeLast()
        }
    }

    fun getCharFromRoom(room: Room) = when (room) {
        is Empty -> ' '
        is Wall -> '#'
        is Wormhole -> 'O'
        is Entrance -> 'S'
        is Exit -> 'E'
        is WithContent -> if (room.content != null) 'T' else ' '
        else -> '?'
    }

    fun initLabyrinth(lab: Labyrinth) {
        val map = Array(lab.height + 2) { Array(lab.width + 2) { mutableStateOf("") } }
        for (y in -1..lab.height) {
            for (x in -1..lab.width) {
                val ch = getCharFromRoom(lab[x, y])
                if (ch == 'S') updateCurrentLocation(Location(x, y))
                map[y + 1][x + 1].value = ch.toString()
            }
        }
        gameState.apply {
            allMaps.add(map)
            allMapsOffsets.add(Location(0, 0))
            labHeight = lab.height + 2
            labWidth = lab.width + 2
            isRunning.value = true
            mapsTotal.value = 1
        }
    }

    fun addEmptyMap() {
        gameState.apply {
            val map = Array(labHeight) { Array(labWidth) { mutableStateOf("") } }
            allMaps.add(map)
            allMapsOffsets.add(currPos!! + Location(1, 1))
            mapsTotal.value++
        }
    }

    fun start() {
        gameState = GameState()
        gameState.apply {
            if (!isRunning.value) {
                thread {
                    val playerRun = object : AbstractPlayerRun() {
                        override fun createPlayer() = HumanAI()
                    }
                    playerRun.doTestLab(pathToLabyrinth)
                    isRunning.value = false
                }
            }
        }
    }
}

abstract class AbstractPlayerRun {

    abstract fun createPlayer(): Player

    fun doTestLab(fileName: String) {
        val lab = Labyrinth.createFromFile(fileName)
        val player = createPlayer()
        val controller = Controller(lab, player)
        sleep(100) // wait for compose to initialize view
        val actualResult = controller.makeMoves(500)
        if (actualResult.exitReached) {
            println("You won!")
        } else {
            println("You lose!")
        }
    }
}

class GameState {
    var currPos: Location? = null
    var prevPos: Location? = null
    val stateOfCurrPose = mutableStateOf(currPos)
    var labHeight = 0
    var labWidth = 0
    lateinit var startLoc: Location
    var allMaps = mutableListOf<Array<Array<MutableState<String>>>>()
    val allMapsOffsets = mutableListOf<Location?>()


}
