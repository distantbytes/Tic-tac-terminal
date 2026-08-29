package page.codeberg.distantbytes.tictacterminal.gameset

class Square(
    val coords: Array<Int>,
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
        return piece.mark.toString()
    }
}