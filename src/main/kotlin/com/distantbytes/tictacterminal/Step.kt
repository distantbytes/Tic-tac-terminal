package com.distantbytes.tictacterminal

sealed class Step

data class Move(val rI: Int, val cI: Int): Step()
// row index (numbers)        bottom to top
// col index (alphabets)      left to right

data class Operation(val value: OperationType): Step() {
    enum class OperationType {
        QUIT,
        TAKEBACK,
        HELP
    }
}
