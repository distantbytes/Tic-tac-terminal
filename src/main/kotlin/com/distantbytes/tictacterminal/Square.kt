package com.distantbytes.tictacterminal

class Square(
    val coords: Move,
    initialPiece: Piece = Piece.BLANK
) {
    var piece = initialPiece
        private set
    fun putDownPiece(pieceToPutDown: Piece): Boolean{
        return if (piece == Piece.BLANK) {
            piece = pieceToPutDown
            true
        } else {
            false
        }
        }

    override fun toString(): String {
        return "[" + piece.mark + "]"
    }
}