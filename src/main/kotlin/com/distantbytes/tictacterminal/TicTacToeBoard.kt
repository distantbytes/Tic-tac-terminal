package com.distantbytes.tictacterminal

class TicTacToeBoard(
    override val size: Int = 3
) : Board(size, size) {
    override val array: Array<Array<Square>>
    init {
        val list = mutableListOf(mutableListOf<Square>())
        for ((row, eachRow) in list.withIndex()) {
            for (i in 1..size) eachRow.add(Square(Move(row, i)))
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