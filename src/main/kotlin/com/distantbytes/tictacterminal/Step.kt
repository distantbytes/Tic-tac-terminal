package com.distantbytes.tictacterminal

sealed class Step

data class Move(val rI: Int, val cI: Int): Step()
// row index (alphabets)    left to right
// col index (numbers)      bottom to top

data class Operation(val value: OperationType): Step() {
    enum class OperationType {
        QUIT,
        TAKEBACK,
        HELP
    }
}
