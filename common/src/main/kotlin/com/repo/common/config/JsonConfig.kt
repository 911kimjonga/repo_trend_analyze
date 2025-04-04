package com.repo.common.config

import com.skp.ocb.kotlin.serialize.serializer.LocalDateSerializer
import com.skp.ocb.kotlin.serialize.serializer.LocalDateTimeSerializer
import com.skp.ocb.kotlin.serialize.serializer.LocalTimeSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonConfig {

    @OptIn(ExperimentalSerializationApi::class)
    @Bean
    fun json(): Json = Json {
        serializersModule = SerializersModule {
            contextual(LocalDateSerializer)
            contextual(LocalTimeSerializer)
            contextual(LocalDateTimeSerializer)
        }
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
        allowSpecialFloatingPointValues = true
        decodeEnumsCaseInsensitive = true
    }

}