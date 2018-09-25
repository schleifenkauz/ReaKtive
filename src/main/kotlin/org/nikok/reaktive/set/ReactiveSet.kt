package org.nikok.reaktive.set

import org.nikok.reaktive.collection.ReactiveCollection

/**
 * A Set of elements which can be observed for modifications
*/
interface ReactiveSet<E> : ReactiveCollection<E, SetChange<E>>
