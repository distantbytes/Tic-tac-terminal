package com.distantbytes.tictacterminal

import java.text.MessageFormat
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream

fun main(args: Array<String>) {
    System.setOut(PrintStream(FileOutputStream(FileDescriptor.out), true, "UTF-8"))
    noArgs()
}

fun noArgs() {
    chooseGame().letsGo()
}
fun chooseGame(): Game {
    println(getBundleString("game.chooseBoard") + " [1, 2, 3]")
    for (textToPrint in arrayOf(
        arrayOf("1", getBundleString("game.classicTicTacToe"), "3-3-3"),
        arrayOf("2", getBundleString("game.standardGomoku"), "15-15-5"),
        arrayOf("3", getBundleString("game.simpleGomoku"), "13-13-5"),
        arrayOf("+", getBundleString("game.custom"), "...")
    )) {
        print(textToPrint[0] + " |\t")
        print(textToPrint[1] + "\t\t")
        print(textToPrint[2] + "\n")
    }
    while (true) {
        print(getBundleString("prompt.yourChoice") + "\t")
        when (readlnOrNull()?.trim()?:"") {
            "1" -> { return TicTacToeGame(3) }
            "2" -> { return MnkGame(15, 5) }
            "3" -> { return MnkGame(13, 5) }
            "+" -> {
                var wantedSize = 0
                var wantedK = 0
                print(MessageFormat.format(getBundleString("prompt.askBoardSize"), "5", "9") + " \t")
                while (wantedSize == 0) {
                    try {
                        when (val readLine = readlnOrNull()?.toInt()) {
                            in 5..19 -> wantedSize = readLine!!
                            else -> print(MessageFormat.format(getBundleString("prompt.enterIntBetween"), "5", "9") + " \t")
                        }
                    } catch (x: Exception) {
                        println(getBundleString("warning.notInt"))
                        Thread.sleep(100)
                        print(MessageFormat.format(getBundleString("prompt.enterIntBetween"), "5", "9") + " \t")
                    }
                }
                print(MessageFormat.format(getBundleString("prompt.howManyToWin"), 5, wantedSize) + " \t")
                while (wantedK == 0) {
                    try {
                        when (val readLine = readlnOrNull()?.toInt()) {
                            in 5..wantedSize -> wantedK = readLine!!
                            else -> print(MessageFormat.format(getBundleString("prompt.enterIntBetween"), "5", "9") + " \t")
                        }
                    } catch (x: Exception) {
                        println(getBundleString("warning.notInt"))
                        Thread.sleep(100)
                        print(MessageFormat.format(getBundleString("prompt.enterIntBetween"), "5", "9") + " \t")
                    }
                }
                return if (wantedK < wantedSize) MnkGame(wantedSize, wantedK) else TicTacToeGame(wantedSize)
            }
            else -> println(getBundleString("warning.notValidOption"))
        }
    }
}
/*
fun parseCustomGame(): Game {
    while (true) {
        println("Enter custom parameters (e.g. \"10-10-6\"): \t")
        readlnOrNull()?.let {
            val parsed = it.trim().split('-')
            if (parsed.size != 3) println("We need three integers for `m`, `n`, and `k`.")
            if (parsed[0] != parsed[1])
        }
    }
}*/
