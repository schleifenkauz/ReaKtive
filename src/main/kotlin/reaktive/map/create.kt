/**
 * @author Nikolaus Knop
 */

package reaktive.map

import reaktive.map.impl.ReactiveMapImpl

fun <K, V> MutableMap<K, V>.reactive(): MutableReactiveMap<K, V> = ReactiveMapImpl(this)

fun <K, V> reactiveMap(entries: Iterable<Pair<K, V>>) = entries.toMap(mutableMapOf()).reactive()

fun <K, V> reactiveMap(vararg entries: Pair<K, V>): MutableReactiveMap<K, V> = reactiveMap(entries.asIterable())