package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.getIndexAlphabet
import kotlin.system.exitProcess

class MnkGame (size: Int = 15, k: Int = 5) : Game {
    private val board = MnkBoard(size, k)
    private val players = setOf(
        CurrentPlayer(),
        MnkBot(board, MnkBot.complexStrategies)
    )
    override fun letsGo() {
        printHints()
        when (gameloop()) {
            Piece.X -> println("Congrats! You win :)")
            Piece.O -> println("Too bad, loser :(")
            else -> println("It's a tie!")
        }
    }
    private fun printHints() {
        println("Get ${board.size} in a row to win.")
        println("Enter these symbols for operations: ")
        for ((token, option) in mapOf(
            '?' to "print hints again",
            '^' to "take a move back",
            '!' to "quit game and exit"
        )) {
            print("$token ($option)  |  ")
        }
        println()
    }
    private fun gameloop(): Piece? {
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
    private fun step(player: Player) {
        while (true) {
            when (val step = player.takeMove()) {
                is Move -> if ( try { board.playOn(step, player.piece) } catch (x: Exception) { println("That's not a valid move! -3"); false } ) return
                is Operation -> when (step.value) {
                    Operation.OperationType.QUIT -> exitProcess(130)
                    Operation.OperationType.HELP -> printHints()
                    Operation.OperationType.TAKEBACK -> if (board.attemptTakeback()) println("Took a move back.") else println("You've just started!")
                }
            }
        }
    }
    private fun pointOutMove(piece: Piece, move: Move) {
        println("$piece played on " + getIndexAlphabet((move.cI + 1).toByte()) + (move.rI + 1) + "\n")
    }

}