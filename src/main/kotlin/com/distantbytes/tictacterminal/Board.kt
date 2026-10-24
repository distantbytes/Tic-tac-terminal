package com.distantbytes.tictacterminal

interface Board {
    fun getSquare(): Square
    fun playOn(move: Move, piece: Piece): Boolean
    fun checkWinner(): Piece?
    override fun toString(): String
    fun attemptTakeback(): Boolean
}