package tictacterminal.gameset

open class Player(
    val thisPiece: Piece,
    private val mat: Mat,
    val prompts: Array<String?> = arrayOf("It's your turn, take a step: ", "${thisPiece.mark} played at square ", "Congratulations! you won:)")
    ) {

    open fun goStep(): Int {
        prompts[0]?.let { println(it) }
        while (true) {
            val blockToPlayAt = getNextStep()
            if (blockToPlayAt.size <= 2 && blockToPlayAt[0] < 0){
                when (blockToPlayAt[0]){
                    -4 -> {
                        if (Toolbox.askYesOrNoQuestion("Are you sure to end the game? ", false)) return -4
                        else println("Take a step, then: ")
                        // TODO: 2026/8/25 gotta put this in the prompts array later. there's a problem with it
                        continue
                    }
                    else -> {/* do nothing */}
                }
            }
            if (mat.playAt(blockToPlayAt[0], blockToPlayAt[1], thisPiece)){
                Thread.sleep(100)
                println(mat.toString())
                println(pointOutStep(mat.getBlock(blockToPlayAt[0], blockToPlayAt[1])!!))
                return 0
            }
        }
    }
    protected open fun getNextStep(): Array<Int>{
        while (true){
            val reply = askPlayerForInput()
            when (reply[0]){
                '!' -> return arrayOf(-4)
                else -> {/* do nothing */}
            }
            try {
                when (  //match the value of resolved,
                    val resolved =
                        if (Toolbox.getAlphabetIndex(reply[0]) != null) resolveCoordRef(reply[0], reply[1].toString().toInt())
                        //if the first character in reply isn't an alphabet letter, we'll get null from getAlphabetIndex
                        else resolveCoordRef(null, reply[0].toString().toInt())
                        //pass either an alphabet character plus an integer, or an integer alone and leave character null
                ) {
                    null -> {
                        println("That's not a valid token! -2")
                    }
                    //when an array is returned which means the token is in correct format, return it. be aware that it might still be out of bound
                    else -> return resolved
                }
            } catch (e: Exception){
                println("That's not a valid token! -3")    //occurs when the byte number we need is not actually a number
            }
        }
    }
    private fun resolveCoordRef(char: Char?, int: Int): Array<Int>?{
        if (char != null) {
            //for situation of an alphabet and an integer, we get the letter's index and use these numbers as coords of rows and columns.
            val charInInt = Toolbox.getAlphabetIndex(char)
            return if (charInInt == null) null else arrayOf(charInInt - 1, int - 1)
            //have them subtracted by 1 because array indexes starts with 0
            //return null if getAlphabetIndex returns null, which happens when the provided character not in alphabet
        } else {
            //if we meet only one integer, simply resolve according to the keypad arrangement on a full keyboard
            return when (int) {
                7 -> arrayOf(0, 0)
                8 -> arrayOf(0, 1)
                9 -> arrayOf(0, 2)
                4 -> arrayOf(1, 0)
                5 -> arrayOf(1, 1)
                6 -> arrayOf(1, 2)
                1 -> arrayOf(2, 0)
                2 -> arrayOf(2, 1)
                3 -> arrayOf(2, 2)
                else -> null
                //return null if integer not between 1 and 9
            }
        }
    }

    private fun askPlayerForInput(): String{
        while (true){
            val playerInput = readLine()
            if (playerInput != null && playerInput.length in 1..2) return playerInput
            //ensure that playerInput as string isn't null and is in proper length
            else println("That's not a valid token! -1")
        }
    }

    open fun pointOutStep(playedAt: Block): String{
        return prompts[1] + Toolbox.getIndexAlphabet((playedAt.pos[0] + 1).toByte())!! + playedAt.pos[1] + "\n"
    }
}