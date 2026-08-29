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
                /*println(true)*/
                true
            }
            movesRec[0].piece == piece -> {
                /*println(false)*/
                false
            }
            else -> {
                movesRec.add(0, IdentifiedMove(piece, move))
                /*println(true)*/
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
                tempMat.playOn(move, piece)
            }
            return mat.attemptTakeback(tempMat)
        }
        return false
    }

    override fun toString(): String {
        return movesRec.toString()
    }
}