package com.distantbytes.tictacterminal

class MnkGame (size: Int = 15, k: Int = 5) : Game(size, k) {
    override val board = MnkBoard(size, k)
    override val players = setOf(
        CurrentPlayer(),
        MnkBot(board, MnkBot.complexStrategies)
    )
}