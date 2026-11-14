package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.sortByValueDescendingWithRandomTies
import kotlin.random.Random

class MnkBot(private val board: Board, private val strategies: Map<(seq: Array<Square>, target: Piece, k: Int) -> Square?, Int> = Companion.simpleStrategies) : Player(Piece.O) {
    override fun takeMove(): Step {
        val extracted = board.extractArray()
        val candidates: MutableMap<Square, Int> = mutableMapOf()
        for (eachDir in extracted) {
            for (eachLined in eachDir) {
                for ((strategy, weight) in strategies) {
                    strategy.invoke(eachLined, Piece.X, board.k)
                        ?.let { candidates.put(it, weight + 1) }
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

    companion object {
        val simpleStrategies = mapOf(
            fun(seq: Array<Square>, target: Piece, _: Int): Square? {
                var countLeft = 0
                var oneLeft: Square? = null
                for (eachSquare in seq){
                    when (eachSquare.piece){
                        Piece.BLANK -> {   //when the same enum value as mOneLeft is matched
                            countLeft++
                            if (countLeft > 1) return null  //check countLeft right away
                            oneLeft = eachSquare
                        }
                        target -> {/*do nothing*/}
                        else -> return null //return null if a third value is discovered
                    }
                }
                return oneLeft  //no need for an if check
            } to 5
        )
    }
}