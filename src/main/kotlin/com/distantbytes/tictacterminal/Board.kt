package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.getIndexAlphabet

abstract class Board (
    open val size: Int,
    open val k: Int
) {
    abstract val array: Array<Array<Square>>
    open fun getSquare(move: Move): Square {
        return array[move.rI][move.cI]
        //throws `IndexOutOfBoundsException`
    }
    open fun playOn(move: Move, piece: Piece): Boolean {
        return getSquare(move).putDownPiece(piece)
        //throws `IndexOutOfBoundsException` from `getSquare()`
    }
    abstract fun checkWinner(): Piece?
    override fun toString(): String {
        var string = ""
        if (size <= 3) {
            for (eachRow in array) {
                string += "\n"
                for (eachSquare in eachRow) {
                    string += "[ $eachSquare ]"
                }
            }
            return string
        } else {
            string += "\t"
            for (i in 1..size) string += getIndexAlphabet(i.toByte()).toString() + "\t"
            for ((i, eachRow) in array.withIndex().reversed()) {
                string += "\n"
                string += i.toString() + "\t"
                for (eachSquare in eachRow) string += eachSquare
            }
            return string
        }
    }
    abstract fun attemptTakeback(): Boolean
}