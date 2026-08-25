package tictacterminal

import tictacterminal.gameset.*
import java.lang.NumberFormatException
import kotlin.math.abs

class TicTacToe {
    fun game() {
        val mat = newMat()
        println("Mat created with size " + mat.size)
        Thread.sleep(500)
        println(mat.toString())
        val playerA = Player(Piece.PLAYER_A, mat)
        val playerB = CongMnkBot(mat) as Player
        Thread.sleep(500)
        printTokenHints(mat.size)
        when (gameLoop(mat, playerA, playerB)) {
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
                println("Game Terminated")
                return
            }
            else -> println("Unexpected result value")
        }

    }

    fun gameLoop(mat: Mat, playerA: Player, playerB: Player): Int {
        var stepCount = 0
        while (true) {
            Thread.sleep(1000)
            when (playerA.goStep()){
                0 -> {
                    stepCount++
                    if (stepCount >= mat.size * mat.size) break
                    if (mat.checkWinningSeq(playerA.thisPiece, mat.extractArray())) return 1
                }
                -1 -> {
                    printTokenHints(mat.size)
                    continue
                }
                -4 -> return -4
            }
            Thread.sleep(1000)
            playerB.goStep()
            stepCount++
            if (stepCount >= mat.size * mat.size) break
            if (mat.checkWinningSeq(playerB.thisPiece, mat.extractArray())) return 2
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

    fun printTokenHints(size: Int) {
        require(size in 1..26)
        println("\nYou can use these tokens as option triggers: ")
        print("|  ")
        for ((token, option) in mapOf(
            '?' to "print hints again",
            '!' to "end game and exit"
        )){
            print("$token -> $option  |  ")
            Thread.sleep(100)
        }
        println("\nTarget the grid cell with the following ones: ")
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

}