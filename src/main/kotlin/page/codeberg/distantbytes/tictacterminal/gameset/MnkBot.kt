package page.codeberg.distantbytes.tictacterminal.gameset

import page.codeberg.distantbytes.tictacterminal.toolbox.printFixedTimeLoadingEllipsis
import page.codeberg.distantbytes.tictacterminal.toolbox.sortByValueDescendingWithRandomTies
import kotlin.random.Random

class MnkBot(private val mat: Mat, override val thisPiece: Piece = Piece.PLAYER_B): Player(thisPiece, mat, arrayOf(
    null,
    "${thisPiece.mark} played on square ",
    "Too bad, loser:(",
    "Thinking"
)) {

    override fun getNextMove(): Move {  //we will return a Move object to represent the coords of the square to play on
        val extracted = mat.extractArray()
        val candidates: MutableMap<Square, Int> = mutableMapOf()    //create an empty map for candidate options
        //find all the almost winning sequences and add the only remaining available square on that line to the candidates
        thinkingDelay()
        for (eachDir in extracted){
            for (eachLined in eachDir){
                findAlmostWinningSeq(eachLined, Piece.PLAYER_A)?.let {
                    //only deal with the the situations when findAlmostWinningSeq returns a valid square
                    candidates.put(it, (candidates[it] ?: 0) + 5) //if the square already exists in candidates, add its map value by 5
                }
                findAlmostWinningSeq(eachLined, thisPiece)?.let {
                    candidates.put(it, (candidates[it] ?: 0) + 5)
                }
                if (mat is BigMat){
                    searchForStraightThree(eachLined, Piece.PLAYER_A, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 4) }
                    searchForStraightThree(eachLined, thisPiece, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 4) }
                    searchForFour(eachLined, Piece.PLAYER_A, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 4)}
                    searchForFour(eachLined, thisPiece, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 4)}
                    searchForLiveThree(eachLined, Piece.PLAYER_A, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 3)}
                    searchForLiveThree(eachLined, thisPiece, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 3)}
                    /*searchForLiveThree(eachLined, thisPiece, mat.k - 1)?.let { candidates.put(it, (candidates[it] ?: 0) + 4)}*/
                    searchForSingle(eachLined, thisPiece)?.let { candidates.put(it, (candidates[it] ?: 0) + 1) }
                    searchForLiveTwo(eachLined, thisPiece, mat.k)?.let { candidates.put(it, (candidates[it] ?: 0) + 2) }
                }
            }
        }
        //now we must sort out the square with the largest map value, and return its coords
        val candidatesSorted = sortByValueDescendingWithRandomTies(candidates).keys
        return if (candidates.isNotEmpty()) Move(candidatesSorted.first().coords[0], candidatesSorted.first().coords[1])
        else {
            val emptySpots = mutableListOf<Square>()
            for (eachRow in mat.matArray){
                for (eachSquare in eachRow){
                    if (eachSquare.piece == Piece.BLANK)
                        emptySpots.add(eachSquare)
                }
            }
            emptySpots.shuffle(Random.Default)
            Move(emptySpots[0].coords[0], emptySpots[1].coords[1])
            //play on random square
        }
    }

    private fun findAlmostWinningSeq(array: Array<Square>, mTarget: Piece): Square?{
        var countLeft = 0
        var oneLeft: Square? = null
        for (eachSquare in array){
            when (eachSquare.piece){
                Piece.BLANK -> {   //when the same enum value as mOneLeft is matched
                    countLeft++
                    if (countLeft > 1) return null  //check countLeft right away
                    oneLeft = eachSquare
                }
                mTarget -> {/*do nothing*/}
                else -> return null //return null if a third value is discovered
            }
        }
        return oneLeft  //no need for an if check
    }
    private fun searchForStraightThree(array: Array<Square>, mTarget: Piece, k: Int): Square?{
        require(mTarget != Piece.BLANK)
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
                mTarget -> {
                    if (seq.size >= 1) seq.add(eachSquare)
                }
                else -> {
                    seq.clear()
                }
            }
        }
        return if (seq.size == k) {
            seq.filter { it.piece == Piece.BLANK }.shuffled()[0]
        } else {
            null
        }
    }
    private fun searchForFour(array: Array<Square>, mTarget: Piece, k: Int): Square?{
        require(mTarget != Piece.BLANK)
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
                mTarget -> {
                    if (seq.size < k) seq.add(eachSquare)
                }
                else -> seq.clear()
            }
        }
        return if (seq.size == k) seq.singleOrNull { it.piece == Piece.BLANK } else null
    }
    private fun searchForLiveThree(array: Array<Square>, mTarget: Piece, k: Int): Square?{
        require(mTarget != Piece.BLANK)
        require(k in 4..array.size)

        val seq = mutableListOf<Square>()
        for (eachSquare in array) {
            seq.add(eachSquare)
            if (   // * _ * *
                seq.size == k - 1
                && seq.count { it.piece == Piece.BLANK } == 1
                && seq[0].piece == mTarget
                && seq.last().piece == mTarget
            ) return seq.singleOrNull { it.piece == Piece.BLANK }
            else if (seq.size == k - 1) seq.removeAt(0)
        }
        for (eachSquare in array) {
            seq.add(eachSquare)
            if (        // _ * * * _
                seq.size == k
                && seq.count { it.piece == mTarget } == k - 2
                && seq[0].piece == Piece.BLANK
                && seq.last().piece == Piece.BLANK
            ) return seq.filter { it.piece == Piece.BLANK }.shuffled()[0]
            else if (seq.size == k) seq.removeAt(0)
        }
        return null
    }
    private fun searchForLiveTwo(array: Array<Square>, mTarget: Piece, k: Int):Square?{
        require(mTarget != Piece.BLANK)
        require(k in 4..array.size)
        val seq = mutableListOf<Square>()
        for (eachSquare in array) {
            seq.add(eachSquare)
            if (        // _ * * _
                seq.size == k - 1
                && seq.count { it.piece == mTarget } == k - 3
                && seq[0].piece == Piece.BLANK
                && seq.last().piece == Piece.BLANK
            ) return seq.filter { it.piece == Piece.BLANK }.shuffled()[0]
            else if (seq.size == k) seq.removeAt(0)
        }
        return null
    }

    private fun searchForSingle(array: Array<Square>, mTarget: Piece): Square?{
        require(mTarget != Piece.BLANK)
        require(array.size > 3)

        val candidates = mutableMapOf<Square, Int>()
        val seq = mutableListOf<Square>()
        for (eachSquare in array){
            seq.add(eachSquare)
            if (
                seq.size == 5
                && seq.count { it.piece == mTarget } in 1..2
                && seq[0].piece == Piece.BLANK
                && seq.last().piece == Piece.BLANK
            ) {
                //_ _ * _ _
                candidates[seq[seq.indexOfFirst { it.piece == mTarget } - 1]] = seq.count { it.piece == mTarget }
                candidates[seq[seq.indexOfLast { it.piece == mTarget } + 1]] = seq.count { it.piece == mTarget }
            } else if (seq.size == 3) seq.removeAt(0)
        }
        return if (candidates.isNotEmpty()) sortByValueDescendingWithRandomTies(candidates).keys.toList()[0] else null
    }

    private fun thinkingDelay(){
        printFixedTimeLoadingEllipsis(5, (250 + mat.size * 50) / 2, prompts[3] ?: "", '.')
    }

    override fun askWhetherToTakeAMoveBack(): Boolean { return false }

}