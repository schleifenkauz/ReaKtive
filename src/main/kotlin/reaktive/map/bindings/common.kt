/**
 * @author Nikolaus Knop
 */

package reaktive.map.bindings

import reaktive.Reactive
import reaktive.map.MapChange.Put
import reaktive.map.MapChange.Removed
import reaktive.map.ReactiveMap
import reaktive.value.ReactiveValue
import reaktive.value.ReactiveVariable
import reaktive.value.binding.binding
import reaktive.value.binding.flatMap

val <K, V> ReactiveMap<K, V>.size
    get() = binding(now.size) {
        addObserver(observeMap { change ->
            if (change is Removed) withValue { set(it - 1) }
            else if (change is Put && change.oldValue == null) withValue { set(it + 1) }
        })
    }

operator fun <K, V> ReactiveMap<K, V>.get(key: K) = binding(now[key]) {
    addObserver(observeMap { change ->
        if (change is Removed && change.key == key) set(null)
        else if (change is Put && change.key == key) set(change.newValue)
    })
}

operator fun <K, V> ReactiveMap<K, V>.get(key: ReactiveValue<K>) = key.flatMap { get(it) }