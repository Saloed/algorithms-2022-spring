package com.example.reversi

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val reversiBack = ReversiBack()

    @Test
    fun moveTest() { // Проверка на правильное совершение хода
        reversiBack.moveFromPlayer(
            Square(2, 3),
            reversiBack.pieceBox,
            reversiBack.possibleMoves,
            reversiBack.whiteTurn,
            reversiBack.blackDisks,
            reversiBack.whiteDisks,
            reversiBack.boardValue,
            true
        )
        assertEquals('W', reversiBack.pieceBox[Square(2, 3)])
    }

    @Test
    fun switchedSideTest() { // Проверка на смену сторон после хода
        val rememberSide = !reversiBack.whiteTurn
        reversiBack.moveFromPlayer(
            Square(2, 3),
            reversiBack.pieceBox,
            reversiBack.possibleMoves,
            reversiBack.whiteTurn,
            reversiBack.blackDisks,
            reversiBack.whiteDisks,
            reversiBack.boardValue,
            true
        )
        assertEquals(rememberSide, reversiBack.whiteTurn)
    }

    @Test
    fun possibleMovesTest() { // Проверка на правильные возможные ходы
        val moves = mutableMapOf<Square, Int>()
        reversiBack.getPossibleMoves(reversiBack.pieceBox, moves)
        assertEquals(moves.isNotEmpty(), true)
        assertNotNull(moves[Square(2, 3)])
        assertNotNull(moves[Square(3, 2)])
        assertNotNull(moves[Square(4, 5)])
        assertNotNull(moves[Square(5, 4)])
    }

    @Test
    fun moveAITest() { // Проверка на совершения хода ИИ
        reversiBack.moveFromPlayer(Square(2, 3), reversiBack.pieceBox, reversiBack.possibleMoves, reversiBack.whiteTurn, reversiBack.blackDisks, reversiBack.whiteDisks, reversiBack.boardValue, true)
        val previousBoard = mutableMapOf<Square, Char>()
        for (piece in reversiBack.pieceBox)
            previousBoard[piece.key] = piece.value
        reversiBack.moveFromAI()
        assertNotEquals(previousBoard, reversiBack.pieceBox)
    }

    @Test
    fun switchSideAITest() { // Проверка на смену сторон после хода ИИ
        reversiBack.moveFromPlayer(Square(2, 3), reversiBack.pieceBox, reversiBack.possibleMoves, reversiBack.whiteTurn, reversiBack.blackDisks, reversiBack.whiteDisks, reversiBack.boardValue, true)
        val rememberSide = !reversiBack.whiteTurn
        reversiBack.moveFromAI()
        assertEquals(rememberSide, reversiBack.whiteTurn)
    }
}