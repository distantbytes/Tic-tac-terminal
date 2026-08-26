package tictacterminal.gameset

import kotlin.random.Random

class CongMnkBot(private val mat: Mat): Player(Piece.PLAYER_B, mat, arrayOf(
    null,
    "X played at square ",
    "Too bad, loser:(",
    "Thinking"
)) {

    override fun getNextStep(): Array<Int>{  //we will return a size-2 array to represent the position to play at
        val extracted = mat.extractArray()
        val candidates: MutableMap<Block, Int> = mutableMapOf()    //create an empty map for candidate options
        //find all the almost winning sequences and add the only remaining available block on that line to the candidates
        thinkingDelay()
        for (eachDir in extracted){
            for (eachLined in eachDir){
                findAlmostWinningSeq(eachLined, Piece.PLAYER_A, Piece.BLANK)?.let {
                    //only deal with the the situations when findAlmostWinningSeq returns a valid Block
                    candidates.put(it, (candidates[it] ?: 0) + 1) //if the block already exists in candidates, add its map value by 1
                }
                findAlmostWinningSeq(eachLined, thisPiece, Piece.BLANK)?.let {
                    candidates.put(it, (candidates[it] ?: 0) + 1)
                }
            }
        }
        //now we must sort out the block with the largest map value, and return its position
        return if (candidates.isNotEmpty()) Toolbox.sortByValueDescendingWithRandomTies(candidates).keys.first().pos
        else {
            val emptySpots = mutableListOf<Block>()
            for (eachRow in mat.matArray){
                for (eachBlock in eachRow){
                    if (eachBlock.piece == Piece.BLANK)
                        emptySpots.add(eachBlock)
                }
            }
            emptySpots.shuffled(Random.Default)[0].pos
            //play at random block
        }
    }

    private fun findAlmostWinningSeq(array: Array<Block>, mTarget: Piece, mOneLeft: Piece): Block?{
        var countLeft = 0
        var oneLeft: Block? = null
        for (eachBlock in array){
            when (eachBlock.piece){
                mOneLeft -> {   //when the same enum value as mOneLeft is matched
                    countLeft++
                    if (countLeft > 1) return null  //check countLeft right away
                    oneLeft = eachBlock
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