package com.repo.integration.naver.datalab.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "integration.naver")
class NaverDatalabConfig {

    lateinit var url: String
    var client: Client = Client()
    var datalab: Datalab = Datalab()

    class Client {
        lateinit var id: String
        lateinit var secret: String
    }

    class Datalab {
        lateinit var path: String
        lateinit var method: String
        lateinit var contentType: String
        lateinit var accept: String
    }

}

