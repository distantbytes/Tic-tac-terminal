package com.distantbytes.tictacterminal

abstract class Board (
    open val size: Int,
    open val k: Int
) {
    abstract val array: Array<Array<Square>>
    abstract fun getSquare(): Square
    abstract fun playOn(move: Move, piece: Piece): Boolean
    abstract fun checkWinner(): Piece?
    abstract override fun toString(): String
    abstract fun attemptTakeback(): Boolean
}