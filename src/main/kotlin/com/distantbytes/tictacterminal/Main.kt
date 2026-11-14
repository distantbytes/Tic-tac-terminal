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
        arrayOf("3", "Simple Five-in-a-row  ", "13-13-5")
        /*
        arrayOf("+", "Customize             ", "...")
        */
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
            else -> println("That's not a valid option! ")
        }
    }
}