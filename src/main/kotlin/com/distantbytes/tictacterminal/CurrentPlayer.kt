package com.distantbytes.tictacterminal

import page.codeberg.distantbytes.tictacterminal.toolbox.getAlphabetIndex

class CurrentPlayer : Player(Piece.X) {
    override fun takeMove(): Step {
        print("Take a move: \t")
        while (true) {
            when (val reply = parseInput(askPlayerForInput())) {
                null -> {
                    println("That's not a valid move! ")
                    Thread.sleep(250)
                    println("Take a move: \t")
                    continue
                }
                is Move -> return reply
                is Operation -> {
                    when (reply.value) {
                        Operation.OperationType.TAKEBACK -> if (askWhetherToTakeback()) return reply else print("Take a move then: \t")
                        else -> return reply
                    }
                }
            }
        }
    }

    private fun askPlayerForInput(): String {
        while (true) {
            val playerInput = readLine()
            if (playerInput != null) return playerInput
            else {
                //ensure that playerInput as string isn't null and is in proper length
                println("That's not a valid move! ")
                Thread.sleep(250)
                print("Take a move: \t")
            }
        }
    }
    private fun parseInput(string: String): Step? {
        if (string.length !in 1..3) return null
        when (string[0]) {
            '?' -> return Operation(Operation.OperationType.HELP)
            '!' -> return Operation(Operation.OperationType.QUIT)
            '^' -> return Operation(Operation.OperationType.TAKEBACK)
            else -> {/* do nothing */}
        }
        if (string.length == 1) {
            try {
                return when (string[0].toInt()) {
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
            } catch (x: Exception) {
                return null
            }

        }
        return try {
            Move(
                getAlphabetIndex(string[0])!! - 1,
                string.drop(1).toInt() - 1
            )
        } catch (x: Exception) {
            null
        }
    }
    fun askWhetherToTakeback(): Boolean {
        print("Take a move back? \t[y/N]:\t")
        return when ((readLine()?.get(0)?.toLowerCase())) {
            'y' -> true
            'n' -> false
            else -> false
        }
    }
}