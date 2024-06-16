package reaktive.impl

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import reaktive.list.ReactiveList
import reaktive.list.impl.UnmodifiableReactiveList

@Suppress("OPT_IN_USAGE")
class UnmodifiableReactiveListSerializer<T>(private val valueSerializer: KSerializer<T>) : KSerializer<ReactiveList<T>> {
    override val descriptor: SerialDescriptor
        get() = listSerialDescriptor(valueSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: ReactiveList<T>) {
        encoder.encodeSerializableValue(ListSerializer(valueSerializer), value.now)
    }

    override fun deserialize(decoder: Decoder): ReactiveList<T> =
        UnmodifiableReactiveList(decoder.decodeSerializableValue(ListSerializer(valueSerializer)))
}