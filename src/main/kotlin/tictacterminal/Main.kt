package tictacterminal

import kotlin.concurrent.thread


fun main(args: Array<String>) {
        val ticTacToe = TicTacToe()
        ticTacToe.game()
        Thread.sleep(1000)
    }