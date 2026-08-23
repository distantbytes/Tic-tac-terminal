package tictactoe_gameset

import kotlin.random.Random

object Toolbox {
/* Someday, I'm gonna put all these in a single library, maybe package, how cool would that be! */

    fun <K> sortByValueDescendingWithRandomTies(map: MutableMap<K, Int>): LinkedHashMap<K, Int>{
        val grouped = map.entries.groupBy { it.value } //group the entries with equal values
        return linkedMapOf<K, Int>().apply {
            //do this to a new linked map
            grouped.keys.sortedDescending().forEach {
                //do this to each key in grouped, in the order of them sorted descending
                    value -> grouped[value]?.shuffled(Random.Default)?.forEach{
                    entry -> put(entry.key, entry.value)    //return this
            }
            }   //return this
        }
    }

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

    fun printFixedTimeLoadingEffect(repeat: Int, delayMilis: Int, string: String, char: Char){
        require(repeat >= 1)
        print(string)
        for (i in 1..repeat){
            Thread.sleep(delayMilis.toLong())
            print(char.toString())
        }
        println()
    }
}