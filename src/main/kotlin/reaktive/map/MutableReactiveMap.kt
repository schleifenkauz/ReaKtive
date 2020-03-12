/**
 * @author Nikolaus Knop
 */

package reaktive.map

interface MutableReactiveMap<K, V>: ReactiveMap<K, V> {
    override val now: MutableMap<K, V>
}