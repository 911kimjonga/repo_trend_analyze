package com.repo.integration.naver.datalab.serializer

import com.repo.integration.naver.datalab.enums.Gender
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object GenderSerializer : KSerializer<Gender> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Gender", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Gender) {
        encoder.encodeString(value.gender)
    }

    override fun deserialize(decoder: Decoder): Gender {
        val value = decoder.decodeString()
        return Gender.entries.find { it.gender == value }
            ?: throw SerializationException("Invalid gender: $value")
    }
}