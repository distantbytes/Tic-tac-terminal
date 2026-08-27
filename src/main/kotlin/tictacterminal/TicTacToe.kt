package tictacterminal

import tictacterminal.gameset.*
import java.lang.NumberFormatException
import kotlin.math.abs

fun main(args: Array<String>) {
    val mat = newMat()
    val history = History(mat)
    println("Mat created with size " + mat.size)
    Thread.sleep(500)
    println(mat.toString())
    val playerA = Player(Piece.PLAYER_A, mat)
    val playerB = CongMnkBot(mat) as Player
    Thread.sleep(500)
    printKeywordHints(mat.size)
    when (gameLoop(mat, history, playerA, playerB)) {
        1 -> {
            Thread.sleep(1000)
            println(playerA.prompts[2])
        }
        2 -> {
            Thread.sleep(1000)
            println(playerB.prompts[2])
        }
        0 -> {
            Thread.sleep(1000)
            println("And it's a tie!")
        }
        -4 -> {
            println("- Game Terminated -")
            return
        }
        else -> println("- Unexpected result value -")
    }
    Thread.sleep(1500)
}

fun gameLoop(mat: Mat, history: History, playerA: Player, playerB: Player): Int {
    /*var movesCount = 0*/
    while (true) {
        Thread.sleep(1000)
        val moveA = playerA.takeMove()
        when {
            moveA.c == null -> {
                /*movesCount++*/
                if (history.movesCount >= mat.size * mat.size) break
                if (mat.checkWinningSeq(playerA.thisPiece, mat.extractArray())) return 1
                history.record(playerA.thisPiece, moveA)
            }
            moveA.c == 'H' -> {
                printKeywordHints(mat.size)
                continue
            }
            moveA.c == 'Q' -> return -4
            moveA.c == 'T' -> {
                if (history.takeback()) {
                    println("Took a move back.")
                    /*movesCount--
                    movesCount--*/
                    println(mat.toString())
                } else println("Something went wrong.")
                continue
            }
        }
        Thread.sleep(1000)
        if (history.movesCount >= mat.size * mat.size) break
        val moveB = playerB.takeMove()
        /*movesCount++*/
        if (mat.checkWinningSeq(playerB.thisPiece, mat.extractArray())) return 2
        history.record(playerB.thisPiece, moveB)
    }
    return when (mat.checkWinningSeq(mat.extractArray())) {
        playerA.thisPiece -> 1
        playerB.thisPiece -> 2
        else -> 0
    }
}

fun newMat(): Mat {
    println("Choose the mat size, or leave blank to go with default: ")
    while (true) {
        when (val wantedSizeInString = readLine()!!) {
            "" -> return Mat()
            else -> {
                try {
                    val wantedSize = wantedSizeInString.toInt()
                    if (abs(wantedSize) in 3..6) return Mat(abs(wantedSize))
                    else println("Please enter an integer between 3 and 6.")
                } catch (e: NumberFormatException) {
                    println("That's not an integer, dumbhead!")
                }
            }
        }
    }
}

fun printKeywordHints(size: Int) {
    require(size in 1..26)
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
    println("\nTarget the grid cell with the following tokens: ")
    if (size > 3) {
        for (eachRow in 1..size) {
            Thread.sleep(100)
            for (i in 1..size) {
                print("  " + Toolbox.getIndexAlphabet(eachRow.toByte()) + i + "   ")
            }
            print("\n")
        }
        println()
    } else {
        for (i in arrayOf(7, 8, 9, 4, 5, 6, 1, 2, 3)) {
            Thread.sleep(100)
            print("   $i   ")
            if (i % 3 == 0) print("\n")
        }
        print("\n")
    }
}
