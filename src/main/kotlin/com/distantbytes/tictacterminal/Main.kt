package com.distantbytes.tictacterminal

fun main(args: Array<String>) {
    noArgs()
}

fun noArgs() {
    chooseGame().letsGo()
}
fun chooseGame(): Game {
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
        print("Your choice:\t")
        when (readlnOrNull()?.trim()?:"") {
            "1" -> { return TicTacToeGame(3) }
            "2" -> { return MnkGame(15, 5) }
            "3" -> { return MnkGame(13, 5) }
            "+" -> {
                var wantedSize = 0
                var wantedK = 0
                print("Specify board size [5 ~ 19]: \t")
                while (wantedSize == 0) {
                    try {
                        when (val readLine = readlnOrNull()?.toInt()) {
                            in 5..19 -> wantedSize = readLine!!
                            else -> print("Please enter an integer between 5 and 19: \t")
                        }
                    } catch (e: Exception) {
                        println("That's not an integer, dumbhead!")
                        Thread.sleep(100)
                        print("Enter an integer between 5 and 19: \t")
                    }
                }
                print("How many in a row to win? [5 ~ $wantedSize]: \t")
                while (wantedK == 0) {
                    try {
                        when (val readLine = readlnOrNull()?.toInt()) {
                            in 5..wantedSize -> wantedK = readLine!!
                            else -> print("Please enter an integer between 5 and $wantedSize: \t")
                        }
                    } catch (e: Exception) {
                        println("That's not an integer, dumbhead!")
                        Thread.sleep(100)
                        print("Enter an integer between 5 and $wantedSize: \t")
                    }
                }
                return if (wantedK < wantedSize) MnkGame(wantedSize, wantedK) else TicTacToeGame(wantedSize)
            }
            else -> println("That's not a valid option! ")
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
