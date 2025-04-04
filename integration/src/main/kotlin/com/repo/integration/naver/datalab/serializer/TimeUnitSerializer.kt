package com.repo.integration.naver.datalab.serializer

import com.repo.integration.naver.datalab.enums.TimeUnit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TimeUnitSerializer : KSerializer<TimeUnit> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TimeUnit", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TimeUnit) {
        encoder.encodeString(value.unit)
    }

    override fun deserialize(decoder: Decoder): TimeUnit {
        val value = decoder.decodeString()
        return TimeUnit.entries.find { it.unit == value }
            ?: throw SerializationException("Invalid time unit: $value")
    }
}