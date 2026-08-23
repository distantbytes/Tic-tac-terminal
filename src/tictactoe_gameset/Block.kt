package tictactoe_gameset

class Block(
/*    val posInCol: Int,
    val posInRow: Int,*/
    val pos: Array<Int>,
    wantedPiece: Piece = Piece.BLANK
) {
    var piece = wantedPiece
        private set
    fun putDownPiece(pieceToPutDown: Piece): Boolean{
        return if (piece == Piece.BLANK) {
            piece = pieceToPutDown
            true
        } else {
            println("That spot is already drawn!")
            false
        }
        }

    override fun toString(): String {
        return " [ " + piece.mark + " ] "
    }
}