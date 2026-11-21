package com.distantbytes.tictacterminal

class CurrentPlayer : Player(Piece.X) {
    override fun takeMove(): Step {
        print(getBundleString("prompt.takeMove") + " \t")
        while (true) {
            when (val reply = parseInput(askPlayerForInput().trim())) {
                null -> {
                    println(getBundleString("warning.notValidMove") + " -2")
                    Thread.sleep(250)
                    print(getBundleString("prompt.takeMove") + " \t")
                    continue
                }
                is Move -> return reply
                is Operation -> {
                    when (reply.value) {
                        Operation.OperationType.TAKEBACK -> if (askWhetherToTakeback()) return reply else print(getBundleString("prompt.takeMove2") + " \t")
                        Operation.OperationType.QUIT ->     if (askWhetherToQuit()) return reply else print(getBundleString("prompt.takeMove2") + " \t")
                        else -> return reply
                    }
                }
            }
        }
    }

    private fun askPlayerForInput(): String {
        while (true) {
            val playerInput = readlnOrNull()
            if (playerInput != null) return playerInput
            else {
                //ensure that playerInput as string isn't null and is in proper length
                println(getBundleString("warning.notValidMove") + " -1")
                Thread.sleep(250)
                print(getBundleString("prompt.takeMove") + " \t")
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
                return when (string[0].toString().toInt()) {
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
                string.drop(1).toInt() - 1,
                getAlphabetIndex(string[0])!! - 1
            )
        } catch (x: Exception) {
            null
        }
    }
    private fun askWhetherToTakeback(): Boolean {
        print(getBundleString("prompt.confirmTakeback") + " \t[y/N]:\t")
        return try {
            when ((readlnOrNull()?.get(0)?.lowercaseChar())) {
                'y' -> true
                'n' -> false
                else -> false
            }
        } catch (x: Exception) { false }
    }
    private fun askWhetherToQuit(): Boolean {
        print(getBundleString("prompt.confirmQuit") + " \t[y/N]:\t")
        return try {
            when ((readlnOrNull()?.get(0)?.lowercaseChar())) {
                'y' -> true
                'n' -> false
                else -> false
            }
        } catch (x: Exception) { false }
    }
}