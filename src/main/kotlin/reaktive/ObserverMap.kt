package reaktive

class ObserverMap<K> {
    private val map = mutableMapOf<K, Observer>()

    operator fun set(key: K, observer: Observer) {
        map[key] = observer
    }

    fun remove(key: K) {
        val observer = map.remove(key) ?: error("No observer found for $key")
        observer.kill()
    }
}