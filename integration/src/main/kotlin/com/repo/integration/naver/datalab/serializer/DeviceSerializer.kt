package com.repo.integration.naver.datalab.serializer

import com.repo.integration.naver.datalab.enums.Device
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DeviceSerializer : KSerializer<Device> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Device", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Device) {
        encoder.encodeString(value.device)
    }

    override fun deserialize(decoder: Decoder): Device {
        val value = decoder.decodeString()
        return Device.entries.find { it.device == value }
            ?: throw SerializationException("Invalid device: $value")
    }
}