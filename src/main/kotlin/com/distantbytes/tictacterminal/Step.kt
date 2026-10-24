package com.distantbytes.tictacterminal

sealed class Step

data class Move(val rI: Int, val cI: Int): Step()

data class Operation(val value: OperationType): Step() {
    enum class OperationType {
        QUIT,
        TAKEBACK
    }
}
