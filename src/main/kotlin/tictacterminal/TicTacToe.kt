package tictacterminal

import tictacterminal.gameset.*
import java.lang.NumberFormatException
import kotlin.math.abs

fun main(args: Array<String>) {
    val mat = newMat()
    println("Mat created with size " + mat.size)
    Thread.sleep(500)
    println(mat.toString())
    val playerA = Player(Piece.PLAYER_A, mat)
    val playerB = CongMnkBot(mat) as Player
    Thread.sleep(500)
    printCoordReferences(mat.size)
    when (gameLoop(mat, playerA, playerB)) {
        playerA -> {
            Thread.sleep(1000)
            println(playerA.textPool[2])
        }
        playerB -> {
            Thread.sleep(1000)
            println(playerB.textPool[2])
        }
        null -> println("And it's a tie!")
    }
    Thread.sleep(1500)
}

fun gameLoop(mat: Mat, playerA: Player, playerB: Player): Player? {
    var stepCount = 0
    while (true) {
        Thread.sleep(1000)
        playerA.goStep()
        stepCount++
        if (stepCount >= mat.size * mat.size) break
        if (mat.checkWinningSeq(playerA.thisPiece, mat.extractArray())) return playerA
        Thread.sleep(1000)
        playerB.goStep()
        stepCount++
        if (stepCount >= mat.size * mat.size) break
        if (mat.checkWinningSeq(playerB.thisPiece, mat.extractArray())) return playerB
    }
    return when (mat.checkWinningSeq(mat.extractArray())) {
        playerA.thisPiece -> playerA
        playerB.thisPiece -> playerB
        else -> null
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

fun printCoordReferences(size: Int) {
    require(size in 1..26)
    println("\nYou can target the grid cell with the following tokens: ")
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
