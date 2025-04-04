package com.skp.ocb.kotlin.serialize.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.time.LocalDateTime", PrimitiveKind.STRING)

    private val formatter01 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    private val formatter02 = DateTimeFormatter.ofPattern("yyyyMMddHH:mm:ss")
    private val formatter03 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return when {
            string.length == 14 && !string.contains("T") -> LocalDateTime.parse(string, formatter01)
            string.length == 16 && !string.contains("T") && string.contains(":") -> LocalDateTime.parse(string, formatter02)
            string.contains("T") -> LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            string.contains(" ") -> LocalDateTime.parse(string, formatter03)
            else -> LocalDateTime.parse(string)
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}