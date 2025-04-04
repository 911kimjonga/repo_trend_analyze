package com.repo.integration.naver.datalab.enums

import com.repo.integration.naver.datalab.serializer.DeviceSerializer
import kotlinx.serialization.Serializable

@Serializable(with = DeviceSerializer::class)
enum class Device(
    val device: String,
) {
    PC("pc"),
    MOBILE("mo"),
    ;
}