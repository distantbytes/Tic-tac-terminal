package com.distantbytes.tictacterminal

class TicTacToeBoard(
    override val size: Int = 3
) : Board(size, size) {
    override val array: Array<Array<Square>>
    init {
        val list = mutableListOf(mutableListOf<Square>())
        /*for ((row, eachRow) in list.withIndex()) {
            for (i in 0 until size) eachRow.add(Square(Move(row, i)))
        }*/
        for (rowIndex in 0 until size) {
            list.add(mutableListOf())
            for (colIndex in 0 until size) list[rowIndex].add(Square(Move(rowIndex, colIndex)))
        }
        array = list.map { it.toTypedArray() }.toTypedArray()
    }
    override fun attemptTakeback(): Boolean {
        if (history.size <= 2) return false
        takeback(history.removeLastOrNull()!!)
        return true
    }
    private fun takeback(record: Pair<Piece, Move>) {
        getSquare(record.second).let {
            if (it.piece != record.first)
            array[it.coords.rI][it.coords.cI] = Square(it.coords)
        }
    }
}