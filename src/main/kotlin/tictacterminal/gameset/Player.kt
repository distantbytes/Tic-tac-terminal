package tictacterminal.gameset

open class Player(
    val thisPiece: Piece,
    private val mat: Mat,
    val prompts: Array<String?> = arrayOf(
        "It's your turn, take a move: ",
        "${thisPiece.mark} played at square ",
        "You win:)",
        null,
        "Are you sure to quit the game? ",
        "Take a move, then: "
    )
    ) {

    open fun takeMove(): Move? {
        prompts[0]?.let { println(it) }
        while (true) {
            val squareToPlayAt = getNextMove()
            if (squareToPlayAt.c != null) {
                when (squareToPlayAt.c){
                    'H' -> return squareToPlayAt
                    'Q' -> {
                        if (Toolbox.askYesOrNoQuestion(prompts[4] ?: "", false)) return squareToPlayAt
                        else println(prompts[5] ?: "")
                        continue
                    }
                    else -> {}
                }
            }
            if (mat.playAt(squareToPlayAt, thisPiece)){
                Thread.sleep(100)
                println(mat.toString())
                println(pointOutMove(mat.getSquare(squareToPlayAt)!!))
                return null
            }
/*
            if (blockToPlayAt.size <= 2 && blockToPlayAt[0] < 0){
                when (blockToPlayAt[0]){
                    -1 -> return -1
                    -4 -> {
                        if (Toolbox.askYesOrNoQuestion(prompts[4] ?: "", false)) return -4
                        else println(prompts[5] ?: "")
                        continue
                    }
                    else -> {}
                }
            }
            if (mat.playAt(blockToPlayAt[0], blockToPlayAt[1], thisPiece)){
                Thread.sleep(100)
                println(mat.toString())
                println(pointOutStep(mat.getBlock(blockToPlayAt[0], blockToPlayAt[1])!!))
                return 0
            }
*/
        }
    }
    protected open fun getNextMove(): Move{
        while (true){
            val reply = askPlayerForInput()
            when (reply[0]){
                '?' -> return Move(c = 'H')
                '!' -> return Move(c = 'Q')
                else -> {/* do nothing */}
            }
            try {
                when (  //match the value of resolved,
                    val resolved =
                        if (Toolbox.getAlphabetIndex(reply[0]) != null) resolveCoordsRef(reply[0], reply[1].toString().toInt())
                        //if the first character in reply isn't an alphabet letter, we'll get null from getAlphabetIndex
                        else resolveCoordsRef(null, reply[0].toString().toInt())
                        //pass either an alphabet character plus an integer, or an integer alone and leave character null
                ) {
                    null -> {
                        println("That's not a valid token! -2")
                    }
                    //when an array is returned which means the token is in correct format, return it. be aware that it might still be out of bound
                    else -> return Move(resolved.rI, resolved.cI)
                }
            } catch (e: Exception){
                println("That's not a valid token! -3")    //occurs when the byte number we need is not actually a number
            }
        }
    }
    private fun resolveCoordsRef(char: Char?, int: Int): Move?{
        if (char != null) {
            //for situation of an alphabet and an integer, we get the letter's index and use these numbers as coords of rows and columns.
            val charInInt = Toolbox.getAlphabetIndex(char)
            return if (charInInt == null) null else Move(charInInt - 1, int - 1)
            //have them subtracted by 1 because array indexes starts with 0
            //return null if getAlphabetIndex returns null, which happens when the provided character not in alphabet
        } else {
            //if we meet only one integer, simply resolve according to the keypad arrangement on a full keyboard
            return when (int) {
                7 -> Move(0, 0)
                8 -> Move(0, 1)
                9 -> Move(0, 2)
                4 -> Move(1, 0)
                5 -> Move(1, 1)
                6 -> Move(1, 2)
                1 -> Move(2, 0)
                2 -> Move(2, 1)
                3 -> Move(2, 2)
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

    open fun pointOutMove(playedAt: Square): String{
        return prompts[1] + Toolbox.getIndexAlphabet((playedAt.coords[0] + 1).toByte())!! + playedAt.coords[1] + "\n"
    }
}