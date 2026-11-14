package com.distantbytes.tictacterminal

class TicTacToeGame(size: Int = 3) : Game (size, size) {
    override val board = TicTacToeBoard(size)
    override val players = setOf(
        CurrentPlayer(),
        MnkBot(board)
    )
}