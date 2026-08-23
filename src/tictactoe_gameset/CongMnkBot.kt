package tictactoe_gameset

import kotlin.random.Random

class CongMnkBot(private val mat: Mat): Player(Piece.PLAYER_B, mat, arrayOf(null, "X played at square ", "Too bad, loser:(")) {

    override fun getNextStep(): Array<Int>{  //we will return a size-2 array to represent the position to play at
        val extracted = mat.extractArray()
        val candidates: MutableMap<Block, Int> = mutableMapOf()    //create an empty map for candidate options
        //find all the lines with opponent almost winning and add the only left block on that line to the candidates so the bot can obstruct them
        thinkingDelay()
        for (eachDir in extracted){
            for (eachLined in eachDir){
                findAlmostStreakPattern(eachLined, Piece.PLAYER_A, Piece.BLANK)?.let {
                    //only deal with the the situations when findAlmostStreakPattern returns a valid Block
                    candidates.put(it, (candidates[it] ?: 0) + 1) //if the block already exists in candidates, add its weight by 1
                }
                findAlmostStreakPattern(eachLined, thisPiece, Piece.BLANK)?.let {
                    candidates.put(it, (candidates[it] ?: 0) + 1)
                }
            }
        }
        //now we must sort out the block with heaviest weight, and return its position
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
        }
    }

    private fun findAlmostStreakPattern(array: Array<Block>, mTarget: Piece, mOneLeft: Piece): Block?{
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
        Toolbox.printFixedTimeLoadingEffect(5, (250 + mat.size * 50), "Thinking", '.')
    }
}