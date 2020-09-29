/**
 *@author Nikolaus Knop
 */

package reaktive.map.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.impl.HandlerCounter
import reaktive.impl.ObserverManager
import reaktive.map.*
import reaktive.set.*
import kotlin.collections.MutableMap.MutableEntry

internal class ReactiveMapImpl<K, V>(private val wrapped: MutableMap<K, V>) : MutableReactiveMap<K, V> {
    private val handlerCounter = HandlerCounter()
    private val manager = ObserverManager<(MapChange<K, V>) -> Unit>(handlerCounter)

    private val _keys = wrapped.keys.toReactiveSet()

    private val _values = wrapped.values.toReactiveSet()

    override val keys: ReactiveSet<K>
        get() = _keys
    override val values: ReactiveCollection<V>
        get() = _values

    override val now = object : AbstractMutableMap<K, V>() {
        override val size: Int
            get() = wrapped.size

        override fun containsKey(key: K): Boolean = wrapped.containsKey(key)

        override fun containsValue(value: V): Boolean = wrapped.containsValue(value)

        override fun get(key: K): V? = wrapped[key]

        override fun isEmpty(): Boolean = wrapped.isEmpty()

        override val entries: MutableSet<MutableEntry<K, V>>
            get() = wrapped.entries

        override val keys: MutableSet<K> = object : MutableSet<K> by wrapped.keys, AbstractMutableSet<K>() {}

        override val values: MutableCollection<V> = object : MutableCollection<V> by wrapped.values, AbstractMutableCollection<V>() {}

        override fun clear() {
            val itr = iterator()
            for ((key, value) in itr) {
                itr.remove()
                fireChange(MapChange.Removed(this@ReactiveMapImpl, key, value))
            }
        }

        override fun put(key: K, value: V): V? {
            val old = wrapped.put(key, value)
            fireChange(MapChange.Put(this@ReactiveMapImpl, key, old, value))
            _keys.now.add(key)
            _values.now.add(value)
            return old
        }

        override fun putAll(from: Map<out K, V>) {
            for ((key, value) in from) put(key, value)
        }

        override fun remove(key: K): V? {
            val old = wrapped.remove(key)
            if (old != null) {
                _keys.now.remove(key)
                _values.now.remove(old)
                fireChange(MapChange.Removed(this@ReactiveMapImpl, key, old))
            }
            return old
        }
    }

    private fun fireChange(change: MapChange<K, V>) {
        manager.notifyHandlers { h -> h.invoke(change) }
    }

    override fun observeMap(handler: (MapChange<K, V>) -> Unit): Observer = manager.addHandler(handler)

    override fun observe(handler: InvalidationHandler): Observer {
        return observeMap { handler.invoke(this) }
    }
}