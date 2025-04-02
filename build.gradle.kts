plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.serialization") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.repo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot starter
    implementation("org.springframework.boot:spring-boot-starter")

    // spring boot web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // logging
    implementation("org.springframework.boot:spring-boot-starter-logging")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.apache.commons:commons-pool2")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // web flux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.113.Final:osx-aarch_64")

    // Serialization (KotlinX)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    // Jackson Kotlin Module
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // jdbc
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // jpa
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // mybatis
//    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
//    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.60.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.60.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.60.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.60.0")
    implementation("org.jetbrains.exposed:exposed-json:0.60.0")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.60.0")

    // h2
    runtimeOnly("com.h2database:h2")

    // tester
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.projectreactor:reactor-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
