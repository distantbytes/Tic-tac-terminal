package tictacterminal.gameset

class BigMat(size: Int, override val k: Int): Mat(size) {

    override fun extractArray(): Array< //contains 3 arrays, each for rows, columns and diagonals
            Array< //contains the same number of arrays as val size, each array is a row, column or diagonal
                    Array<Square> //row, column or diagonal, contains nullable squares
                    >
            >{

        //extract all lines and throw them in an array
        val rows = Array(size){ row -> matArray[row].copyOf() }

        // now do the same to the columns
        val cols = Array(size){ col -> Array(size){ row -> matArray[row][col] } }

        // and the diagonals
        val diags = mutableListOf(
            Array(size){ i -> matArray[i][i] }, //this is the main diag, from top-left to bottom-right, change index in both ways
            Array(size){ i -> matArray[i][size - (i + 1)] } // this is the anti diag, from top-right to bottom-left, change second index in the opposite direction
        )
        diags.addAll(extractHalfDiagonals(matArray, k, true, true))
        diags.addAll(extractHalfDiagonals(matArray, k, true, false))
        diags.addAll(extractHalfDiagonals(matArray, k, false, true))
        diags.addAll(extractHalfDiagonals(matArray, k, false, false))

        //finally, put the three arrays into one
        return arrayOf(rows, cols, diags.toTypedArray())
    }

    private fun extractHalfDiagonals(matrix: Array<Array<Square>>, k: Int, dColP: Boolean, fromTop: Boolean): Array<Array<Square>>{
        require(matrix.all { it.size == matrix.size })
        require(k in 1..matrix.size)
        val extractedDiags = mutableListOf<Array<Square>>()

        if (fromTop){
            val colRange = if (dColP) 1 until matrix.size else 0 until matrix.size - 1
            for (eachCol in colRange){
                val diag = mutableListOf<Square>()
                var rI = 0
                var cI = eachCol
                while (rI < matrix.size && cI in matrix.indices){
                    diag.add(matrix[rI++][if (dColP) cI++ else cI--])
                }
                if (diag.size >= k) extractedDiags.add(diag.toTypedArray())
            }
        } else {
            for (eachRow in 1 until matrix.size){
                val diag = mutableListOf<Square>()
                var rI = eachRow
                var cI = if (dColP) 0 else matrix.size - 1
                while (rI < matrix.size && cI in matrix.indices){
                    diag.add(matrix[rI++][if (dColP) cI++ else cI--])
                }
                if (diag.size >= k) extractedDiags.add(diag.toTypedArray())
            }
        }

        return extractedDiags.sortedByDescending { it.size }.toTypedArray()
    }

    override fun checkWinningSeq(extracted: Array<Array<Array<Square>>>): Piece?{
        for (eachDir in extracted){
            for (eachLined in eachDir){
                val seq = mutableListOf<Piece>()
                for (eachSquare in eachLined){
                    seq.add(eachSquare.piece)
                    if (seq.size == k) {
                        if (seq.all { it == seq[0] && it != Piece.BLANK }) return seq[0]
                        else seq.removeAt(0)
                    }
                }
                //keep an eye on this
            }

        }
        return null
    }
}