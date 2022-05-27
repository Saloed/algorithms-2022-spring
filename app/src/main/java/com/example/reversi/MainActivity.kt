package com.example.reversi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), ReversiConnector {

    private val reversiBack = ReversiBack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val reversiFront: ReversiFront = findViewById(R.id.chess_view)
        reversiFront.reversiConnector = this
        restartButton()
        findViewById<Button>(R.id.restart_button).setOnClickListener {
            restartButton()
            reversiBack.restart()
            reversiFront.invalidate()
        }
    }

    private fun restartButton() {
        MaterialAlertDialogBuilder(this)
            .setTitle("New game")
            .setMessage("Choose side")
            .setNeutralButton("Play as black") { _, _ ->
                reversiBack.skipFirstWhiteTurn = true
            }
            .setPositiveButton("Play as white") { _, _ ->
            }
            .setCancelable(false)
            .show()
    }

    override fun square(x: Int, y: Int): SquarePiece {
        return reversiBack.square(x, y)!!
    }

    override fun moveFromPlayer(moveFromPlayer: Square) {
        reversiBack.moveFromPlayer(
            Square(moveFromPlayer.x, moveFromPlayer.y), reversiBack.pieceBox,
            reversiBack.possibleMoves, reversiBack.whiteTurn, reversiBack.blackDisks,
            reversiBack.whiteDisks, reversiBack.boardValue, true
        )
        if (reversiBack.announceWhiteWin) Toast.makeText(
            applicationContext,
            "White win! \nBlack Disks = ${reversiBack.blackDisks[0]}, White Disks = ${reversiBack.whiteDisks[0]}",
            Toast.LENGTH_LONG
        ).show()
        if (reversiBack.announceBlackWin) Toast.makeText(
            applicationContext,
            "Black win! \nBlack Disks = ${reversiBack.blackDisks[0]}, White Disks = ${reversiBack.whiteDisks[0]}",
            Toast.LENGTH_LONG
        ).show()
        if (reversiBack.announcePat) Toast.makeText(
            applicationContext,
            "Everyone lost! \nBlack Disks = ${reversiBack.blackDisks[0]}, White Disks = ${reversiBack.whiteDisks[0]}",
            Toast.LENGTH_LONG
        ).show()
        if (!reversiBack.announceWhiteWin && !reversiBack.announceBlackWin && !reversiBack.announcePat && reversiBack.skipTurn) Toast.makeText(
            applicationContext,
            "No possible moves, skip turn",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun returnPossibleMoves(): MutableMap<Square, Int> {
        return reversiBack.possibleMoves
    }

    override fun moveFromAI() {
        reversiBack.moveFromAI()
    }
}