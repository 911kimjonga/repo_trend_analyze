package com.repo.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalTimeSerializer : KSerializer<LocalTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.time.LocalDateTime", PrimitiveKind.STRING)

    private val flatFormatter = DateTimeFormatter.ofPattern("HHmmss")

    override fun deserialize(decoder: Decoder): LocalTime {
        val string = decoder.decodeString()
        return when (string.length) {
            6 -> LocalTime.parse(string, flatFormatter)
            else -> LocalTime.parse(string, DateTimeFormatter.ISO_LOCAL_TIME)
        }
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME))
    }
}
