package com.example.reversi

import kotlin.math.max
import kotlin.math.min

class ReversiBack {
    val pieceBox = mutableMapOf<Square, SquarePiece>() // Список фигур на доске
    var whiteTurn = true // Переменная для проверки того, кто ходит
    val possibleMoves = mutableMapOf<Square, Int>() // Возможные ходы
    val boardValue = arrayOf(0)
    private var whiteWin = false
    private var blackWin = false
    private var pat = false
    var announceWhiteWin = false
    var announceBlackWin = false
    var announcePat = false
    var skipTurn = false
    private var doubleSkipTurn = false
    var skipFirstWhiteTurn = false
    val blackDisks = arrayOf(2)
    val whiteDisks = arrayOf(2)
    private var inCycle = false
    private var successfulMove = false
    private val pieceValue = mapOf(
        Pair(Square(0, 0), 20000), Pair(Square(1, 0), -3000), Pair(Square(2, 0), 1000), Pair(Square(3, 0), 800), Pair(Square(4, 0), 800), Pair(Square(5, 0), 1000), Pair(Square(6, 0), -3000), Pair(Square(7, 0), 20000),
        Pair(Square(0, 1), -3000), Pair(Square(1, 1), -5000), Pair(Square(2, 1), -450), Pair(Square(3, 1), -500), Pair(Square(4, 1), -500), Pair(Square(5, 1), -450), Pair(Square(6, 1), -5000), Pair(Square(7, 1), -3000),
        Pair(Square(0, 2), 1000), Pair(Square(1, 2), -450), Pair(Square(2, 2), 30), Pair(Square(3, 2), 10), Pair(Square(4, 2), 10), Pair(Square(5, 2), 30), Pair(Square(6, 2), -450), Pair(Square(7, 2), 1000),
        Pair(Square(0, 3), 800), Pair(Square(1, 3), -500), Pair(Square(2, 3), 10), Pair(Square(3, 3), 50), Pair(Square(4, 3), 50), Pair(Square(5, 3), 10), Pair(Square(6, 3), -500), Pair(Square(7, 3), 800),
        Pair(Square(0, 4), 800), Pair(Square(1, 4), -500), Pair(Square(2, 4), 10), Pair(Square(3, 4), 50), Pair(Square(4, 4), 50), Pair(Square(5, 4), 10), Pair(Square(6, 4), -500), Pair(Square(7, 4), 800),
        Pair(Square(0, 5), 1000), Pair(Square(1, 5), -450), Pair(Square(2, 5), 30), Pair(Square(3, 5), 10), Pair(Square(4, 5), 10), Pair(Square(5, 5), 30), Pair(Square(6, 5), -450), Pair(Square(7, 5), 1000),
        Pair(Square(0, 6), -3000), Pair(Square(1, 6), -5000), Pair(Square(2, 6), -450), Pair(Square(3, 6), -500), Pair(Square(4, 6), -500), Pair(Square(5, 6), -450), Pair(Square(6, 6), -5000), Pair(Square(7, 6), -3000),
        Pair(Square(0, 7), 20000), Pair(Square(1, 7), -3000), Pair(Square(2, 7), 1000), Pair(Square(3, 7), 800), Pair(Square(4, 7), 800), Pair(Square(5, 7), 1000), Pair(Square(6, 7), -3000), Pair(Square(7, 7), 20000),
    )

    init {
        restart()
    }

    fun getPossibleMoves(
        pieceBox: MutableMap<Square, SquarePiece>,
        possibleMoves: MutableMap<Square, Int>
    ) {
        var addMove: Boolean
        var k: Int
        for (i in 0..7)
            for (j in 0..7) {
                if (pieceBox[Square(i, j)] == SquarePiece.NONE) {
                    k = 1 // Проверка вправо
                    addMove = false
                    while (i + k <= 7 && pieceBox[Square(
                            i + k,
                            j
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && addMove && pieceBox[Square(
                            i + k,
                            j
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] = 1
                    }
                    k = 1 // Проверка влево
                    addMove = false
                    while (i - k >= 0 && pieceBox[Square(
                            i - k,
                            j
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && addMove && pieceBox[Square(
                            i - k,
                            j
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 10
                    }
                    k = 1 // Проверка вверх
                    addMove = false
                    while (j + k <= 7 && pieceBox[Square(
                            i,
                            j + k
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (j + k <= 7 && addMove && pieceBox[Square(
                            i,
                            j + k
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 100
                    }
                    k = 1 // Проверка вниз
                    addMove = false
                    while (j - k >= 0 && pieceBox[Square(
                            i,
                            j - k
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (j - k >= 0 && addMove && pieceBox[Square(
                            i,
                            j - k
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 1000
                    }
                    k = 1 // Проверка вправо-вверх
                    addMove = false
                    while (i + k <= 7 && j + k <= 7 && pieceBox[Square(
                            i + k,
                            j + k
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && j + k <= 7 && addMove && pieceBox[Square(
                            i + k,
                            j + k
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 10000
                    }
                    k = 1 // Проверка влево-вверх
                    addMove = false
                    while (i - k >= 0 && j + k <= 7 && pieceBox[Square(
                            i - k,
                            j + k
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && j + k <= 7 && addMove && pieceBox[Square(
                            i - k,
                            j + k
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 100000
                    }
                    k = 1 // Проверка вправо-вниз
                    addMove = false
                    while (i + k <= 7 && j - k >= 0 && pieceBox[Square(
                            i + k,
                            j - k
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && j - k >= 0 && addMove && pieceBox[Square(
                            i + k,
                            j - k
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 1000000
                    }
                    k = 1 // Проверка влево-вниз
                    addMove = false
                    while (i - k >= 0 && j - k >= 0 && pieceBox[Square(
                            i - k,
                            j - k
                        )] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE
                    ) {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && j - k >= 0 && addMove && pieceBox[Square(
                            i - k,
                            j - k
                        )] == if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                    ) {
                        possibleMoves[Square(i, j)] =
                            possibleMoves.getOrElse(Square(i, j)) { 0 } + 10000000
                    }
                }
            }
    }

    fun moveFromPlayer(
        attemptToMove: Square, pieceBox: MutableMap<Square, SquarePiece>,
        possibleMoves: MutableMap<Square, Int>, whiteTurn: Boolean, blackDisks: Array<Int>,
        whiteDisks: Array<Int>, boardValue: Array<Int>, lastMove: Boolean
    ) {
        if (skipFirstWhiteTurn) return
        if (skipTurn) {
            this.whiteTurn = !whiteTurn
            getPossibleMoves(pieceBox, possibleMoves)
        }
        if (attemptToMove in possibleMoves) {
            whiteWin = false
            blackWin = false
            pat = false
            successfulMove = true
            val x = attemptToMove.x
            val y = attemptToMove.y
            pieceBox[Square(x, y)] = if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
            if (whiteTurn) {
                whiteDisks[0]++
                boardValue[0] = boardValue[0] + pieceValue[Square(x, y)]!!
            } else {
                blackDisks[0]++
                boardValue[0] = boardValue[0] - pieceValue[Square(x, y)]!!
            }
            var k: Int
            for (i in 1..8) {
                k = 1
                if (possibleMoves[Square(x, y)]!! % 10 == 1) {
                    var newX = when (i) {
                        1 -> x + k
                        2 -> x - k
                        3 -> x
                        4 -> x
                        5 -> x + k
                        6 -> x - k
                        7 -> x + k
                        else -> x - k
                    }
                    var newY = when (i) {
                        1 -> y
                        2 -> y
                        3 -> y + k
                        4 -> y - k
                        5 -> y + k
                        6 -> y + k
                        7 -> y - k
                        else -> y - k
                    }
                    while (pieceBox[Square(newX, newY)] == if (whiteTurn) SquarePiece.BLACK else SquarePiece.WHITE) {
                        pieceBox[Square(newX, newY)] = if (whiteTurn) SquarePiece.WHITE else SquarePiece.BLACK
                        if (whiteTurn) {
                            blackDisks[0]--
                            whiteDisks[0]++
                            boardValue[0] += 2 * pieceValue[Square(newX, newY)]!!
                        } else {
                            blackDisks[0]++
                            whiteDisks[0]--
                            boardValue[0] -= 2 * pieceValue[Square(newX, newY)]!!
                        }
                        k++
                        newX = when (i) {
                            1 -> x + k
                            2 -> x - k
                            3 -> x
                            4 -> x
                            5 -> x + k
                            6 -> x - k
                            7 -> x + k
                            else -> x - k
                        }
                        newY = when (i) {
                            1 -> y
                            2 -> y
                            3 -> y + k
                            4 -> y - k
                            5 -> y + k
                            6 -> y + k
                            7 -> y - k
                            else -> y - k
                        }
                    }
                }
                if (possibleMoves[Square(x, y)] == 0) break
                else possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
            }
            possibleMoves.clear()
            doubleSkipTurn = skipTurn
            skipTurn = false
            this.whiteTurn = !whiteTurn
            getPossibleMoves(pieceBox, possibleMoves)
            if (possibleMoves.isEmpty()) {
                skipTurn = true
                this.whiteTurn = !whiteTurn
                getPossibleMoves(pieceBox, possibleMoves)
                if (whiteDisks[0] + blackDisks[0] == 64) {
                    if (blackDisks[0] > whiteDisks[0]) {
                        if (lastMove) announceBlackWin = true
                        blackWin = true
                    }
                    if (whiteDisks[0] > blackDisks[0]) {
                        if (lastMove) announceWhiteWin = true
                        whiteWin = true
                    }
                    if (whiteDisks[0] == blackDisks[0]) {
                        if (lastMove) announcePat = true
                        pat = true
                    }
                }
            }
        } else successfulMove = false
    }

    fun moveFromAI() {
        if (skipTurn) {
            this.whiteTurn = !whiteTurn
            getPossibleMoves(pieceBox, possibleMoves)
        }
        if (successfulMove && !skipTurn || !successfulMove && skipTurn || skipFirstWhiteTurn) {
            if (skipTurn) {
                whiteTurn = !whiteTurn
            }
            skipFirstWhiteTurn = false
            inCycle = true
            val rememberWhiteTurn = whiteTurn
            var bestMove =
                if (possibleMoves.isNotEmpty()) possibleMoves.keys.first() else Square(-1, -1)
            var bestScore = if (rememberWhiteTurn) Int.MIN_VALUE else Int.MAX_VALUE
            val startTime = System.currentTimeMillis()
            var algDepth = 2
            while (System.currentTimeMillis() - startTime < 3000) {
                for (move in possibleMoves) {
                    val newPieceBox = pieceBox.toMutableMap()
                    val newPossibleMoves = possibleMoves.toMutableMap()
                    val newWhiteDisks = arrayOf(whiteDisks[0])
                    val newBlackDisks = arrayOf(blackDisks[0])
                    val newBoardValue = arrayOf(boardValue[0])
                    moveFromPlayer(
                        move.key, newPieceBox, newPossibleMoves, rememberWhiteTurn,
                        newBlackDisks, newWhiteDisks, newBoardValue, false
                    )
                    val scoreAI = minimax(
                        pieceBox, possibleMoves, whiteDisks, blackDisks, boardValue, algDepth,
                        arrayOf(Int.MIN_VALUE), arrayOf(Int.MAX_VALUE), whiteTurn, startTime
                    )
                    if (rememberWhiteTurn) if (scoreAI > bestScore) {
                        bestScore = scoreAI
                        bestMove = move.key
                    }
                    if (!rememberWhiteTurn) if (scoreAI < bestScore) {
                        bestScore = scoreAI
                        bestMove = move.key
                    }
                }
                algDepth++
            }
            whiteTurn = rememberWhiteTurn
            inCycle = false
            moveFromPlayer(
                bestMove, pieceBox, possibleMoves, whiteTurn,
                blackDisks, whiteDisks, boardValue, true
            )
        }
    }

    private fun minimax(
        pieceBox: MutableMap<Square, SquarePiece>, possibleMoves: MutableMap<Square, Int>,
        whiteDisks: Array<Int>, blackDisks: Array<Int>, boardValue: Array<Int>, depth: Int,
        alpha: Array<Int>, beta: Array<Int>, whiteTurn: Boolean, startTime: Long
    ): Int {
        var bestScore: Int
        if (depth == 0 || whiteWin || blackWin || pat || System.currentTimeMillis() - startTime > 3000)
            return boardValue[0]
        bestScore = if (whiteTurn) Int.MIN_VALUE else Int.MAX_VALUE
        for (move in possibleMoves.keys) {
            val newPieceBox = pieceBox.toMutableMap()
            val newPossibleMoves = possibleMoves.toMutableMap()
            val newWhiteDisks = arrayOf(whiteDisks[0])
            val newBlackDisks = arrayOf(blackDisks[0])
            val newBoardValue = arrayOf(boardValue[0])
            moveFromPlayer(
                move, newPieceBox, newPossibleMoves, !whiteTurn,
                newBlackDisks, newWhiteDisks, newBoardValue, false
            )
            val score = minimax(
                newPieceBox, newPossibleMoves, newWhiteDisks, newBlackDisks,
                newBoardValue, depth - 1, alpha, beta, !whiteTurn, startTime
            )
            if (whiteTurn) {
                if (score >= bestScore) bestScore = score
                if (beta[0] < max(alpha[0], bestScore)) break
            } else {
                if (score <= bestScore) bestScore = score
                if (min(beta[0], bestScore) < alpha[0]) break
            }
        }
        return bestScore
    }

    fun square(x: Int, y: Int): SquarePiece? {
        return pieceBox[Square(x, y)]
    }

    fun restart() {
        whiteTurn = true
        pieceBox.clear()
        possibleMoves.clear()
        blackWin = false
        whiteWin = false
        announceBlackWin = false
        announcePat = false
        announceWhiteWin = false
        pat = false
        skipTurn = false
        blackDisks[0] = 2
        whiteDisks[0] = 2
        boardValue[0] = 0
        for (i in 0..7)
            for (j in 0..7) {
                pieceBox[Square(i, j)] = SquarePiece.NONE
            }
        pieceBox[Square(3, 4)] = SquarePiece.WHITE
        pieceBox[Square(3, 3)] = SquarePiece.BLACK
        pieceBox[Square(4, 3)] = SquarePiece.WHITE
        pieceBox[Square(4, 4)] = SquarePiece.BLACK
        getPossibleMoves(pieceBox, possibleMoves)
    }
}