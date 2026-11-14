package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.getIndexAlphabet

abstract class Board (
    open val size: Int,
    open val k: Int
) {
    abstract val array: Array<Array<Square>>
    open val history = mutableListOf<Pair<Piece, Move>>()
    open fun getSquare(move: Move): Square {
        return array[move.rI][move.cI]
        //throws `IndexOutOfBoundsException`
    }
    open fun playOn(move: Move, piece: Piece): Boolean {
        return getSquare(move).putDownPiece(piece).let {
            if (it) history.add(Pair(piece, move))
            it
        }
        //throws `IndexOutOfBoundsException` from `getSquare()`
    }
    open fun checkWinner(): Piece? {
        for (eachDir in extractArray()) {
            for (eachLined in eachDir) {
                when (val seq = eachLined.map{ it.piece }.distinct().singleOrNull()) {
                    null -> continue
                    Piece.BLANK -> continue
                    else -> return seq
                }
            }
        }
        return null
    }
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
            string += "\n\t"
            for (i in 1..size) string += getIndexAlphabet(i.toByte()).toString() + "  "
            for ((i, eachRow) in array.withIndex().reversed()) {
                string += "\n"
                string += (i + 1).toString() + "\t"
                for (eachSquare in eachRow) string += "$eachSquare  "
            }
            string += "\n"
            return string
        }
    }
    open fun extractArray(): Array< //contains 3 arrays, each for rows, columns and diagonals
            Array< //contains the same number of arrays as val size, each array is a row, column or diagonal
                    Array<Square> //row, column or diagonal
                    >
            > {
        val size = this.size
        //extract all lines and throw them in an array
        val rows = Array(size){ row -> this.array[row].copyOf() }

        // now do the same to the columns
        val cols = Array(size){ col -> Array(size){ row -> this.array[row][col] } }

        // and the diagonals
        val diags = arrayOf(
            Array(size){ i -> this.array[i][i] }, //this is the main diag, from top-left to bottom-right, change index in both ways
            Array(size){ i -> this.array[i][size - (i + 1)] } // this is the anti diag, from top-right to bottom-left, change second index in the opposite direction
        )

        //finally, put the three arrays into one
        return arrayOf(rows, cols, diags)
    }
    abstract fun attemptTakeback(): Boolean
}