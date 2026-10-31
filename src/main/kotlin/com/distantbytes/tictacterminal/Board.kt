package com.distantbytes.tictacterminal

interface Board {
    val size: Int
    val k: Int
    val array: Array<Array<Square>>
    fun getSquare(): Square
    fun playOn(move: Move, piece: Piece): Boolean
    fun checkWinner(): Piece?
    override fun toString(): String
    fun attemptTakeback(): Boolean
}