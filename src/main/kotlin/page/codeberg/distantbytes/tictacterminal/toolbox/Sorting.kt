package page.codeberg.distantbytes.tictacterminal.toolbox

import kotlin.random.Random

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