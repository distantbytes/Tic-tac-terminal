package com.distantbytes.tictacterminal

import java.util.Locale
import java.util.ResourceBundle
import kotlin.random.Random

fun getBundleString(key: String): String = ResourceBundle
    .getBundle(
        "tictacterminal",
        Locale.getDefault()
    )!!
    .getString(key)

fun getAlphabetIndex(char: Char): Int?{
    return when (char){
        in 'A'..'Z' -> char - 'A' + 1
        in 'a'..'z' -> char - 'a' + 1
        else -> null
    }
}
fun getIndexAlphabet(byte: Byte): Char? {
    return when (byte) {
        in 1..26 -> 'A' + (byte - 1)
        else -> null
    }
}

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