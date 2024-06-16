package reaktive.impl

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import reaktive.value.*

internal class ReactiveVariableSerializer<T>(private val valueSerializer: KSerializer<T>) : KSerializer<ReactiveVariable<T>> {
    override val descriptor: SerialDescriptor
        get() = valueSerializer.descriptor

    override fun deserialize(decoder: Decoder): ReactiveVariable<T> =
        reactiveVariable(decoder.decodeSerializableValue(valueSerializer))

    override fun serialize(encoder: Encoder, value: ReactiveVariable<T>) {
        encoder.encodeSerializableValue(valueSerializer, value.now)
    }
}