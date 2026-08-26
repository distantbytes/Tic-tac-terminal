package tictacterminal.gameset

// TODO: 2026/8/26 add comments for this file
class History(private val mat: Mat) {
    data class IdentifiedMove(val piece: Piece, val move: Move)

    private val movesRec = mutableListOf<IdentifiedMove>()
    val movesCount: Int
        get() = movesRec.size

    fun record(piece: Piece, move: Move): Boolean {
        return when {
            movesRec.isEmpty() -> {
                movesRec.add(IdentifiedMove(piece, move))
                /*println(movesRec.size)*/
                true
            }
            movesRec[0].piece == piece -> {
                /*println(movesRec.size)*/
                false
            }
            else -> {
                movesRec.add(0, IdentifiedMove(piece, move))
                /*println(movesRec.size)*/
                true
            }
        }
    }
    fun takeback(): Boolean{
        if (movesRec.size > 2) {
            val tempMat = Mat(mat.size)
            movesRec.removeAt(0)
            movesRec.removeAt(0)
            for ((piece, move) in movesRec.reversed()) {
                tempMat.playAt(move, piece)
            }
            /*println(movesRec.size)*/
            return mat.attemptTakeback(tempMat)
        }
        return false
    }

}