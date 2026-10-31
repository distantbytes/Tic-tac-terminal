package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.sortByValueDescendingWithRandomTies
import kotlin.random.Random

class MnkBot(private val board: Board, private val strategies: Map<(seq: Array<Square>, target: Piece, k: Int) -> Square?, Int>) : Player(Piece.O) {
    override fun takeMove(): Step {
        val extracted = extractArray(board)
        val candidates: MutableMap<Square, Int> = mutableMapOf()
        for (eachDir in extracted) {
            for (eachLined in eachDir) {
                for ((strategy, weight) in strategies) {
                    strategy.invoke(eachLined, Piece.X, board.k)
                        ?.let { candidates.put(it, weight) }
                    strategy.invoke(eachLined, piece, board.k)
                        ?.let { candidates.put(it, weight) }
                }
            }
        }
        sortByValueDescendingWithRandomTies(candidates).keys
            .let {
                return if (it.isNotEmpty()) it.first().coords else {
                    val emptySpots = mutableListOf<Square>()
                    for (eachRow in board.array){
                        for (eachSquare in eachRow){
                            if (eachSquare.piece == Piece.BLANK)
                                emptySpots.add(eachSquare)
                        }
                    }
                    emptySpots.shuffle(Random.Default)
                    emptySpots.first().coords
                    //play on random square
                }
            }
    }

    fun extractArray(board: Board): Array< //contains 3 arrays, each for rows, columns and diagonals
            Array< //contains the same number of arrays as val size, each array is a row, column or diagonal
                    Array<Square> //row, column or diagonal, contains nullable squares
                    >
            > {
        val size = board.size
        //extract all lines and throw them in an array
        val rows = Array(size){ row -> board.array[row].copyOf() }

        // now do the same to the columns
        val cols = Array(size){ col -> Array(size){ row -> board.array[row][col] } }

        // and the diagonals
        val diags = arrayOf(
            Array(size){ i -> board.array[i][i] }, //this is the main diag, from top-left to bottom-right, change index in both ways
            Array(size){ i -> board.array[i][size - (i + 1)] } // this is the anti diag, from top-right to bottom-left, change second index in the opposite direction
        )

        //finally, put the three arrays into one
        return arrayOf(rows, cols, diags)
    }
}