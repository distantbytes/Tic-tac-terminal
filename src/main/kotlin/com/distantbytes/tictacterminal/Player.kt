package com.distantbytes.tictacterminal

abstract class Player (
    open val piece: Piece
) {
    abstract fun takeMove(): Step
}