package tictacterminal.gameset

import java.lang.IndexOutOfBoundsException

class Mat(
    val size: Int = 3
) {
    val matArray = createMat(size)

    fun extractArray(): Array< //contains 3 arrays, each for rows, columns and diagonals
            Array< //contains the same number of arrays as val size, each array is a row, column or diagonal
                    Array<Block> //row, column or diagonal, contains nullable Blocks
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
    fun getBlock(move: Move): Block?{
        return try {
            matArray[move.rI][move.cI]
        } catch (e: IndexOutOfBoundsException){
            null
        }
    }

    fun checkWinningSeq(extracted: Array<Array<Array<Block>>>): Piece?{
        for (eachDir in extracted){
            for (eachLined in eachDir){
                val countSamePieces = mutableMapOf(Piece.PLAYER_A to 0, Piece.PLAYER_B to 0)
                for (eachBlock in eachLined){
                    when(eachBlock.piece){
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
    fun checkWinningSeq(tPiece: Piece, extracted: Array<Array<Array<Block>>>): Boolean{
        return checkWinningSeq(extracted) == tPiece
    }

/*    fun getMat(): Array<Array<Block>>{
        return matArray.copyOf()
    }*/
    fun playAt(move: Move, piece: Piece): Boolean{
    return try {
        matArray[move.rI][move.cI].putDownPiece(piece)
    } catch (e: IndexOutOfBoundsException){
        println("You're drawing out of bounds!")
        false
    }
/*        return try {
            if (matArray[ri][ci] == null) {
                matArray[ri][ci] = block
                true
            } else {
                println("That spot is already taken.")
                false
            }
        } catch (e: IndexOutOfBoundsException){
            println("You are drawing out of bound.")
            false
        }*/
    }

    override fun toString(): String{
        var matForPrint = ""
        var boolean = false
        for (eachRow in matArray){
            if (boolean) matForPrint += "\n"
            for (eachBlock in eachRow){
                matForPrint += eachBlock.toString()
            }
            boolean = true
        }
        return matForPrint
    }

    private fun createMat(size: Int): Array<Array<Block>>{
        //magic code! Don't touch!
        val array: Array<Array<Block>> = Array(size) { Array(size){Block(emptyArray())} }
        for ((rowCount, eachRow) in array.withIndex()){
            for ((itemCount) in eachRow.withIndex()){
                array[rowCount][itemCount] = Block(arrayOf(rowCount, itemCount))
            }   //to make sure each block's position value matches its actual position
        }
        return array
    }
}


