package page.codeberg.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.gameset.*

fun main(args: Array<String>) {
    val mat = newMat()
    val history = History(mat)
    println("Board size of " + mat.size)
    Thread.sleep(500)
    println(mat.toString())
    val playerA = Player(Piece.PLAYER_A, mat)
    val playerB = MnkBot(mat) as Player
    Thread.sleep(500)
    printKeywordHints(mat.size, mat.k)
    while (true) {
        when (gameLoop(mat, history, playerA, playerB)) {
            1 -> {
                Thread.sleep(1000)
                println(playerA.prompts[2])
                if (playerB.askWhetherToTakeAMoveBack() && history.takeback()){
                    println("Took a move back.")
                    println(mat.toString())
                    continue
                } else break
            }
            2 -> {
                Thread.sleep(1000)
                println(playerB.prompts[2])
                if (playerA.askWhetherToTakeAMoveBack() && history.takeback()){
                    println("Took a move back.")
                    println(mat.toString())
                    continue
                } else break
            }
            0 -> {
                Thread.sleep(1000)
                println("And it's a tie!")
                break
            }
            -4 -> {
                println("- Game Terminated -")
                break
            }
            else -> {
                println("- Unexpected result value -")
                break
            }
        }
    }
    Thread.sleep(1500)
}

fun gameLoop(mat: Mat, history: History, playerA: Player, playerB: Player): Int {
    while (true) {
        /*println(history)*/
        Thread.sleep(1000)
        val moveA = playerA.takeMove()
        when (moveA.c) {
            null -> {
                if (history.movesCount >= mat.size * mat.size) break
                history.record(playerA.thisPiece, moveA)
                if (mat.checkWinningSeq(playerA.thisPiece, mat.extractArray())) return 1
            }
            'H' -> {
                printKeywordHints(mat.size)
                continue
            }
            'Q' -> return -4
            'T' -> {
                if (history.takeback()) {
                    println("Took a move back.")
                    println(mat.toString())
                } else println("Something went wrong.")
                continue
            }
        }
        Thread.sleep(1000)
        if (history.movesCount >= mat.size * mat.size) break
        val moveB = playerB.takeMove()
        history.record(playerB.thisPiece, moveB)
        if (mat.checkWinningSeq(playerB.thisPiece, mat.extractArray())) return 2
        /*println(history.movesCount)*/
    }
    return when (mat.checkWinningSeq(mat.extractArray())) {
        playerA.thisPiece -> 1
        playerB.thisPiece -> 2
        else -> 0
    }
}

fun newMat(): Mat {
    println("Choose a board to play on: [1, 2, 3]")
    for (textToPrint in arrayOf(
        arrayOf("1", "Classic Tic-tac-toe   ", "3-3-3"),
        arrayOf("2", "Standard Five-in-a-row", "15-15-5"),
        arrayOf("3", "Simple Five-in-a-row  ", "13-13-5"),
        arrayOf("+", "Customize             ", "...")
    )) {
        print(textToPrint[0] + " |\t")
        print(textToPrint[1] + "\t\t")
        print(textToPrint[2] + "\n")
    }
    while (true) {
        Thread.sleep(670)
        print("Your choice: \t")
        when (readLine()!!.toString()) {
            "1" -> return Mat(3)
            "2" -> return BigMat(15, 5)
            "3" -> return BigMat(13, 5)
            "+" -> {
                var wantedSize = 0
                var wantedK = 0
                print("Specify board size [5 ~ 19] : ")
                while (wantedSize == 0) {
                    try {
                        when (val readLine = readLine()?.toInt()) {
                            in 5..19 -> wantedSize = readLine!!
                            else -> print("Please enter an integer between 5 and 19: ")
                        }
                    } catch (e: Exception) {
                        println("That's not an integer, dumbhead!")
                        Thread.sleep(100)
                        print("Enter an integer between 5 and 19: ")
                    }
                }
                print("How many in a row to win? [5 ~ $wantedSize] : ")
                while (wantedK == 0) {
                    try {
                        when (val readLine = readLine()?.toInt()) {
                            in 5..wantedSize -> wantedK = readLine!!
                            else -> print("Please enter an integer between 5 and $wantedSize: ")
                        }
                    } catch (e: Exception) {
                        println("That's not an integer, dumbhead!")
                        Thread.sleep(100)
                        print("Enter an integer between 5 and $wantedSize: ")
                    }
                }
                return if (wantedK < wantedSize) BigMat(wantedSize, wantedK) else Mat(wantedSize)
            }

            else -> println("Enter one of the options!")
        }
    }
}

fun printKeywordHints(size: Int, k: Int = 3) {
    require(size in 1..26)
    println("\nGet $k in a row to win!")
    Thread.sleep(1000)
    println("\nYou can use these keywords for options: ")
    print("|  ")
    for ((token, option) in mapOf(
        '?' to "print hints again",
        '^' to "take a move back",
        '!' to "quit game and exit"
    )) {
        print("$token -> $option  |  ")
        Thread.sleep(100)
    }
    if (size <= 3) {
        println("\nTarget a grid with these coordinates: ")
        for (i in arrayOf(7, 8, 9, 4, 5, 6, 1, 2, 3)){
            print(" $i\u3000 ")
            if (i % 3 == 0) print("\n")
        }
    } else {
        println("\nTarget a grid with board coordinates.")
    }
    println()
}
