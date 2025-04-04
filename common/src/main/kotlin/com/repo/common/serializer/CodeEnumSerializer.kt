package com.repo.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

class CodeEnumSerializer<T>(
    private val valueClass: KClass<T>,
    private val valueOfCode: (String) -> T,
    private val codeExtractor: (T) -> String
) : KSerializer<T> where T : Enum<T>, T : CodeEnum {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(valueClass.simpleName ?: "Enum", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(codeExtractor(value))
    }

    override fun deserialize(decoder: Decoder): T {
        val code = decoder.decodeString()
        return try {
            valueOfCode(code)
        } catch (e: Exception) {
            throw SerializationException("Unknown code for enum ${valueClass.simpleName}: $code")
        }
    }
}