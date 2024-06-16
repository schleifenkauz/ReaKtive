package reaktive.impl

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import reaktive.value.ReactiveValue
import reaktive.value.now
import reaktive.value.reactiveValue

internal class ReactiveValueSerializer<T>(private val valueSerializer: KSerializer<T>) : KSerializer<ReactiveValue<T>> {
    override val descriptor: SerialDescriptor
        get() = valueSerializer.descriptor

    override fun deserialize(decoder: Decoder): ReactiveValue<T> =
        reactiveValue(decoder.decodeSerializableValue(valueSerializer))

    override fun serialize(encoder: Encoder, value: ReactiveValue<T>) {
        encoder.encodeSerializableValue(valueSerializer, value.now)
    }
}