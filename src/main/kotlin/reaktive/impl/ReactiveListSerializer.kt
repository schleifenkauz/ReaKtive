package reaktive.impl

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import reaktive.list.MutableReactiveList
import reaktive.list.ReactiveList
import reaktive.list.impl.ReactiveListImpl
import reaktive.list.impl.UnmodifiableReactiveList

@Suppress("OPT_IN_USAGE")
class ReactiveListSerializer<T>(private val valueSerializer: KSerializer<T>) : KSerializer<MutableReactiveList<T>> {
    override val descriptor: SerialDescriptor
        get() = listSerialDescriptor(valueSerializer.descriptor)

    override fun serialize(encoder: Encoder, value: MutableReactiveList<T>) {
        encoder.encodeSerializableValue(ListSerializer(valueSerializer), value.now)
    }

    override fun deserialize(decoder: Decoder): MutableReactiveList<T> =
        ReactiveListImpl(decoder.decodeSerializableValue(ListSerializer(valueSerializer)).toMutableList())
}