/**
 * @author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import reaktive.value.ReactiveValue
import reaktive.value.ReactiveVariable

fun <T> ReactiveVariable<T>.asProperty(): Property<T> = PropertyAdapter(this)

fun <T> ReactiveValue<T>.asObservableValue(): ObservableValue<T> = ObservableValueAdapter(this)

fun <T> Property<T>.asReactiveVariable(): ReactiveVariable<T> = ReactiveVariableAdapter(this)

fun <T> ObservableValue<T>.asReactiveValue(): ReactiveValue<T> = ReactiveValueAdapter(this)