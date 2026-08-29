package page.codeberg.distantbytes.tictacterminal.toolbox

fun printFixedTimeLoadingEllipsis(repeat: Int, delayMilis: Int, string: String, char: Char){
    require(repeat >= 1)
    print(string)
    for (i in 1..repeat){
        Thread.sleep(delayMilis.toLong())
        print(char.toString())
    }
    println()
}
fun askYesOrNoQuestion(prompt: String, default: Boolean): Boolean{
    println(prompt + if (default) "[Y/n]" else "[y/N]")
    val answer = readLine()
    if (answer == "") return default
    answer?.let {
        return when (it[0].toUpperCase()){
            'Y' -> true
            'N' -> false
            else -> default
        }
    }
    return default
}