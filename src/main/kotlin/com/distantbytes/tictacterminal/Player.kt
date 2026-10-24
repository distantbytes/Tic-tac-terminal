package com.distantbytes.tictacterminal

interface Player {
    val piece: Piece
    fun takeMove(): Step
    val board: Board
}