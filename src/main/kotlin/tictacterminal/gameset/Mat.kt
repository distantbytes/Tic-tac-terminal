package tictacterminal.gameset

import java.lang.IndexOutOfBoundsException

open class Mat(
    val size: Int = 3
) {
    var matArray = createMat(size)
        private set
    open val k = size

    open fun extractArray(): Array< //contains 3 arrays, each for rows, columns and diagonals
            Array< //contains the same number of arrays as val size, each array is a row, column or diagonal
                    Array<Square> //row, column or diagonal, contains nullable squares
                    >
            >{
        require(size in 3..6)

        //extract all lines and throw them in an array
        val rows = Array(size){ row -> matArray[row].copyOf() }

        // now do the same to the columns
        val cols = Array(size){ col -> Array(size){ row -> matArray[row][col] } }

        // and the diagonals
        val diags = arrayOf(
            Array(size){ i -> matArray[i][i] }, //this is the main diag, from top-left to bottom-right, change index in both ways
            Array(size){ i -> matArray[i][size - (i + 1)] } // this is the anti diag, from top-right to bottom-left, change second index in the opposite direction
        )

        //finally, put the three arrays into one
        return arrayOf(rows, cols, diags)
    }
    fun getSquare(move: Move): Square?{
        return try {
            matArray[move.rI][move.cI]
        } catch (e: IndexOutOfBoundsException){
            null
        }
    }

    open fun checkWinningSeq(extracted: Array<Array<Array<Square>>>): Piece?{
        for (eachDir in extracted){
            for (eachLined in eachDir){
                val countSamePieces = mutableMapOf(Piece.PLAYER_A to 0, Piece.PLAYER_B to 0)
                for (eachSquare in eachLined){
                    when(eachSquare.piece){
                        Piece.PLAYER_A -> countSamePieces[Piece.PLAYER_A] = countSamePieces[Piece.PLAYER_A]!! + 1
                        Piece.PLAYER_B -> countSamePieces[Piece.PLAYER_B] = countSamePieces[Piece.PLAYER_B]!! + 1
                        Piece.BLANK -> continue
                    }
                }
                for ((piece, int) in countSamePieces){
                    if (int == eachLined.size) return piece
                }
            }
        }
        return null
    }
    fun checkWinningSeq(tPiece: Piece, extracted: Array<Array<Array<Square>>>): Boolean{
        return checkWinningSeq(extracted) == tPiece
    }

    fun playAt(move: Move, piece: Piece): Boolean{
    return try {
        matArray[move.rI][move.cI].putDownPiece(piece)
    } catch (e: IndexOutOfBoundsException){
        println("You're drawing out of bounds!")
        false
    }
    }

    override fun toString(): String{
        var matForPrint = ""
        var boolean = false
        for (eachRow in matArray){
            if (boolean) matForPrint += "\n"
            for (eachSquare in eachRow){
                matForPrint += eachSquare.toString()
            }
            boolean = true
        }
        return matForPrint
    }

    private fun createMat(size: Int): Array<Array<Square>>{
        //magic code! Don't touch!
        val array: Array<Array<Square>> = Array(size) { Array(size){Square(emptyArray())} }
        for ((rowCount, eachRow) in array.withIndex()){
            for ((itemCount) in eachRow.withIndex()){
                array[rowCount][itemCount] = Square(arrayOf(rowCount, itemCount))
            }   //to make sure each square's coords value matches
        }
        return array
    }

    fun attemptTakeback(mat: Mat): Boolean{
        return if (mat.size == size){
            matArray = mat.matArray
            true
        } else false
    }
}


