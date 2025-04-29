package com.repo.common.config

import com.repo.common.serializer.LocalDateSerializer
import com.repo.common.serializer.LocalDateTimeSerializer
import com.repo.common.serializer.LocalTimeSerializer
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