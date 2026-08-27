package tictacterminal.gameset

import kotlin.random.Random

class CongMnkBot(private val mat: Mat): Player(Piece.PLAYER_B, mat, arrayOf(
    null,
    "X played at square ",
    "Too bad, loser:(",
    "Thinking"
)) {

    override fun getNextMove(): Move{  //we will return a Move object to represent the coords of the square to play at
        val extracted = mat.extractArray()
        val candidates: MutableMap<Square, Int> = mutableMapOf()    //create an empty map for candidate options
        //find all the almost winning sequences and add the only remaining available square on that line to the candidates
        thinkingDelay()
        for (eachDir in extracted){
            for (eachLined in eachDir){
                findAlmostWinningSeq(eachLined, Piece.PLAYER_A, Piece.BLANK)?.let {
                    //only deal with the the situations when findAlmostWinningSeq returns a valid square
                    candidates.put(it, (candidates[it] ?: 0) + 1) //if the square already exists in candidates, add its map value by 1
                }
                findAlmostWinningSeq(eachLined, thisPiece, Piece.BLANK)?.let {
                    candidates.put(it, (candidates[it] ?: 0) + 1)
                }
            }
        }
        //now we must sort out the square with the largest map value, and return its coords
        return if (candidates.isNotEmpty()) Move(Toolbox.sortByValueDescendingWithRandomTies(candidates).keys.first().coords[0], Toolbox.sortByValueDescendingWithRandomTies(candidates).keys.first().coords[1])
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
            //play at random square
        }
    }

    private fun findAlmostWinningSeq(array: Array<Square>, mTarget: Piece, mOneLeft: Piece): Square?{
        var countLeft = 0
        var oneLeft: Square? = null
        for (eachSquare in array){
            when (eachSquare.piece){
                mOneLeft -> {   //when the same enum value as mOneLeft is matched
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

    private fun thinkingDelay(){
        Toolbox.printFixedTimeLoadingEllipsis(5, (250 + mat.size * 50), prompts[3] ?: "", '.')
    }
}