package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.getIndexAlphabet
import kotlin.system.exitProcess
import java.text.MessageFormat

abstract class Game (size: Int, k: Int) {
    abstract val board: Board
    abstract val players: Set<Player>
    open fun letsGo() {
        printHints()
        when (gameloop()) {
            Piece.X -> println(getBundleString("closing.youWin"))
            Piece.O -> println(getBundleString("closing.youLose"))
            else -> println(getBundleString("closing.tie"))
        }
    }
    open fun printHints() {
        println(MessageFormat.format(getBundleString("hint.inARow"), board.k))
        println(getBundleString("hint.symbols"))
        for ((symbol, operation) in mapOf(
            '?' to getBundleString("hint.printHints"),
            '^' to getBundleString("hint.takeback"),
            '!' to getBundleString("hint.quit")
        )) {
            print("$symbol ($operation)  |  ")
        }
        println()
    }
    open fun gameloop(): Piece? {
        println(board)
        while (true) {
            for (eachPlayer in players) {
                step(eachPlayer)
                if (board.history.size >= board.size * board.size) return null
                print(board)
                pointOutMove(board.history.last().first, board.history.last().second)
                board.checkWinner()?.let { return it }
            }
        }
    }
    open fun step(player: Player) {
        while (true) {
            when (val step = player.takeMove()) {
                is Move -> if (try {
                        board.playOn(step, player.piece) 
                    } catch (x: Exception) {
                        println(getBundleString("warning.outOfBoard"))
                        continue 
                    }) return else println(
                    getBundleString("warning.squareTaken")
                )
                is Operation -> when (step.value) {
                    Operation.OperationType.QUIT -> exitProcess(130)
                    Operation.OperationType.HELP -> printHints()
                    Operation.OperationType.TAKEBACK -> if (board.attemptTakeback()) {
                        println(getBundleString("warning.tookBack"))
                        println(board)
                    } else println(
                        getBundleString("warning.notEnoughMoves")
                    )
                }
            }
        }
    }
    open fun pointOutMove(piece: Piece, move: Move) {
        println(MessageFormat.format(getBundleString("hint.playedOn"), piece, getIndexAlphabet((move.cI + 1).toByte())!!, (move.rI + 1)) + "\n")
    }

}