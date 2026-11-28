package com.distantbytes.tictacterminal

import kotlin.random.Random

class MnkBot(private val board: Board, private val strategies: Map<(seq: Array<Square>, target: Piece, k: Int) -> List<Square>, Int> = simpleStrategies) : Player(Piece.O) {
    override fun takeMove(): Step {
        val extracted = board.extractArray()
        val candidates: MutableMap<Square, Int> = mutableMapOf()
        print(getBundleString("bot.thinking"))
        /*for (eachDir in extracted) {
            for (eachLined in eachDir) {
                print(".")
                for ((strategy, weight) in strategies) {
                    strategy.invoke(eachLined, Piece.X, board.k)
                        ?.let { candidates.put(it, weight + 1) }
                    strategy.invoke(eachLined, piece, board.k)
                        ?.let { candidates.put(it, weight) }
                }
            }
        }*/
        for ((strategy, weight) in strategies) {
            for (eachDir in extracted) {
                for (eachLined in eachDir) {
                    strategy.invoke(eachLined, Piece.X, board.k)
                        .let {
                            for (eachSquare in it)
                            candidates.put(eachSquare, (candidates[eachSquare] ?: 0) + weight + 1) 
                        }
                    strategy.invoke(eachLined, piece, board.k)
                        .let {
                            for (eachSquare in it)
                            candidates.put(eachSquare, (candidates[eachSquare] ?: 0) + weight) 
                        }
                }
            }
            print(".")
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

            fun(seq: Array<Square>, target: Piece, _: Int): List<Square> {
                var countLeft = 0
                lateinit var oneLeft: Square
                for (eachSquare in seq){
                    when (eachSquare.piece){
                        Piece.BLANK -> {   //when the same enum value as mOneLeft is matched
                            countLeft++
                            if (countLeft > 1) return emptyList()  //check countLeft right away
                            oneLeft = eachSquare
                        }
                        target -> {/*do nothing*/}
                        else -> return emptyList() //return null if a third value is discovered
                    }
                }
                return listOf(oneLeft)  //no need for an if check
            } to 5

        )

        val complexStrategies = mapOf(

            fun(array: Array<Square>, target: Piece, k: Int): List<Square>{  //straight three
                require(target != Piece.BLANK)
                require(k in 5..array.size)
                //the sequence of a straight three is: _ * * * _
                val seq = mutableListOf<Square>()
                for (eachSquare in array) {
                    if (seq.size == k) break
                    when (eachSquare.piece) {
                        Piece.BLANK -> {
                            if (seq.isEmpty() || seq.size == k - 1) seq.add(eachSquare)
                            else {
                                seq.clear()
                                seq.add(eachSquare)
                            }
                        }
                        target -> {
                            if (seq.size >= 1) seq.add(eachSquare)
                        }
                        else -> {
                            seq.clear()
                        }
                    }
                }
                return if (seq.size == k) {
                    seq.filter { it.piece == Piece.BLANK }
                } else {
                    emptyList()
                }
            } to 25,

            fun(array: Array<Square>, target: Piece, k: Int): List<Square>{  //four
                require(target != Piece.BLANK)
                require(k in 5..array.size)
                //the sequence of an open four is: * * * * _

                val seq = mutableListOf<Square>()
                for (eachSquare in array){
                    if (seq.size >= k) break
                    when (eachSquare.piece){
                        Piece.BLANK -> {
                            if (seq.none { it.piece == Piece.BLANK }) seq.add(eachSquare)
                            else {
                                seq.clear()
                                seq.add(eachSquare)
                            }
                        }
                        target -> {
                            if (seq.size < k) seq.add(eachSquare)
                        }
                        else -> seq.clear()
                    }
                }
                //return if (seq.size == k) seq.singleOrNull { it.piece == Piece.BLANK } else null
                return seq.takeIf { it.size == k }
                    ?.let { listOfNotNull(it.singleOrNull { s -> s.piece == Piece.BLANK }) }
                    .orEmpty()
            } to 16,

            fun(array: Array<Square>, target: Piece, k: Int): List<Square>{  //live three
                require(target != Piece.BLANK)
                require(k in 4..array.size)

                val seq = mutableListOf<Square>()
                for (eachSquare in array) {
                    seq.add(eachSquare)
                    if (   // * _ * *
                        seq.size == k - 1
                        && seq.count { it.piece == Piece.BLANK } == 1
                        && seq[0].piece == target
                        && seq.last().piece == target
                    ) return listOfNotNull(seq.singleOrNull { it.piece == Piece.BLANK })
                    else if (seq.size == k - 1) seq.removeAt(0)
                }
                /*for (eachSquare in array) {
                    seq.add(eachSquare)
                    if (        // _ * * * _
                        seq.size == k
                        && seq.count { it.piece == target } == k - 2
                        && seq[0].piece == Piece.BLANK
                        && seq.last().piece == Piece.BLANK
                    ) return seq.filter { it.piece == Piece.BLANK }.shuffled()[0]
                    else if (seq.size == k) seq.removeAt(0)
                }*/
                return emptyList()
            } to 9,

            fun(array: Array<Square>, target: Piece, k: Int): List<Square>{  //live two
                require(target != Piece.BLANK)
                require(k in 4..array.size)
                val seq = mutableListOf<Square>()
                for (eachSquare in array) {
                    seq.add(eachSquare)
                    if (        // _ * * _
                        seq.size == k - 1
                        && seq.count { it.piece == target } == k - 3
                        && seq[0].piece == Piece.BLANK
                        && seq.last().piece == Piece.BLANK
                    ) return seq.filter { it.piece == Piece.BLANK }
                    else if (seq.size == k - 1) seq.removeAt(0)
                }
                return emptyList()
            } to 4,

            fun(array: Array<Square>, mTarget: Piece, _:Int): List<Square>{
                require(mTarget != Piece.BLANK)
                require(array.size > 3)

                val candidates = mutableMapOf<Square, Int>()
                val seq = mutableListOf<Square>()
                for (eachSquare in array){
                    seq.add(eachSquare)
                    if (
                        seq.size == 3
                        && seq.count { it.piece == mTarget } == 1
                        && seq[0].piece == Piece.BLANK
                        && seq.last().piece == Piece.BLANK
                    ) {
                        //_ _ * _ _
                        candidates[seq[seq.indexOfFirst { it.piece == mTarget } - 1]] = seq.count { it.piece == mTarget }
                        candidates[seq[seq.indexOfLast { it.piece == mTarget } + 1]] = seq.count { it.piece == mTarget }
                    } else if (seq.size == 3) seq.removeAt(0)
                }
                return if (candidates.isNotEmpty()) candidates.keys.toList() else emptyList()
            } to 1
        )
    }
}