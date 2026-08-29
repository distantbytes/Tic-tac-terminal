package page.codeberg.distantbytes.tictacterminal.gameset

import page.codeberg.distantbytes.tictacterminal.toolbox.askYesOrNoQuestion
import page.codeberg.distantbytes.tictacterminal.toolbox.getAlphabetIndex
import page.codeberg.distantbytes.tictacterminal.toolbox.getIndexAlphabet

open class Player(
    open val thisPiece: Piece,
    private val mat: Mat,
    val prompts: Array<String?> = arrayOf(
        "It's your turn, take a move: \t",
        "${thisPiece.mark} played on square ",
        "You win:)",
        null,
        "Are you sure to ",
        "Take a move, then: \t",
        "Take a move back? ",
        "quit the game? ",
        "take that back? "
    )
    ) {

    open fun takeMove(): Move {
        prompts[0]?.let { print(it) }
        while (true) {
            val squareToPlayOn = getNextMove()
            if (squareToPlayOn.c != null) {
                when (squareToPlayOn.c){
                    'H' -> return squareToPlayOn
                    'Q' -> {
                        if (askYesOrNoQuestion((prompts[4] ?: "") + prompts[7], false)) return squareToPlayOn
                        else println(prompts[5] ?: "")
                        continue
                    }
                    'T' -> {
                        if (askYesOrNoQuestion((prompts[4] ?: "") + prompts[8], false)) return squareToPlayOn
                        else println(prompts[5] ?: "")
                        continue
                    }
                    else -> {}
                }
            }
            if (mat.playOn(squareToPlayOn, thisPiece)){
                Thread.sleep(100)
                println(mat.toString())
                println(pointOutMove(mat.getSquare(squareToPlayOn)!!))
                return squareToPlayOn
            } else {
                Thread.sleep(250)
                print(prompts[5])
            }
        }
    }
    protected open fun getNextMove(): Move {
        while (true){
            val reply = askPlayerForInput()
            when (reply[0]){
                '?' -> return Move(c = 'H')
                '!' -> return Move(c = 'Q')
                '^' -> return Move(c = 'T')
                else -> {/* do nothing */}
            }
            try {
                when (  //match the value of resolved,
                    val resolved =
                        if (getAlphabetIndex(reply[0]) != null) resolveCoordsRef(reply[0], reply.drop(1).toInt())
                        //if the first character in reply isn't an alphabet letter, we'll get null from getAlphabetIndex
                        else resolveCoordsRef(null, reply.toInt())
                        //pass either an alphabet character plus an integer, or an integer alone and leave character null
                ) {
                    null -> {
                        println("That's not a valid token! -2")
                        Thread.sleep(250)
                        print(prompts[5])
                    }
                    //when an array is returned which means the token is in correct format, return it. be aware that it might still be out of bound
                    else -> return Move(resolved.rI, resolved.cI)
                }
            } catch (e: Exception){
                println("That's not a valid token! -3")    //occurs when the byte number we need is not actually a number
                Thread.sleep(250)
                print(prompts[5])
            }
        }
    }
    private fun resolveCoordsRef(char: Char?, int: Int): Move?{
        if (char != null) {
            //for situation of an alphabet and an integer, we get the letter's index and use these numbers as coords of rows and columns.
            val charInInt = getAlphabetIndex(char)
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
            if (playerInput != null && playerInput.length in 1..3) return playerInput
            else {
                //ensure that playerInput as string isn't null and is in proper length
                println("That's not a valid token! -1")
                Thread.sleep(250)
                print(prompts[5])
            }
        }
    }

    open fun pointOutMove(playedOn: Square): String{
        return prompts[1] + getIndexAlphabet((playedOn.coords[0] + 1).toByte())!! + (playedOn.coords[1] + 1) + "\n"
    }

    open fun askWhetherToTakeAMoveBack(): Boolean{
        Thread.sleep(500)
        return askYesOrNoQuestion(prompts[6] ?: "", false)
    }
}