package page.codeberg.distantbytes.tictacterminal.toolbox

fun getAlphabetIndex(char: Char): Int?{
    return when (char){
        in 'A'..'Z' -> char - 'A' + 1
        in 'a'..'z' -> char - 'a' + 1
        else -> null
    }
}
fun getIndexAlphabet(byte: Byte): Char?{
    return when (byte){
        in 1..26 -> 'A' + (byte - 1)
        else -> null
    }
}