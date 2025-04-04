package com.repo.integration.naver.datalab.serializer

import com.repo.integration.naver.datalab.enums.Ages
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RequestDataAgesSerializer : KSerializer<List<Ages>> {
    override val descriptor: SerialDescriptor =
        ListSerializer(String.serializer()).descriptor

    override fun serialize(encoder: Encoder, value: List<Ages>) {
        val codes = value.map { it.code }
        encoder.encodeSerializableValue(ListSerializer(String.serializer()), codes)
    }

    override fun deserialize(decoder: Decoder): List<Ages> {
        val codes = decoder.decodeSerializableValue(ListSerializer(String.serializer()))
        return codes.map { code ->
            Ages.entries.find { it.code == code }
                ?: throw SerializationException("Invalid Ages code: $code")
        }
    }
}