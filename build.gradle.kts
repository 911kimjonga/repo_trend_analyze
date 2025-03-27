plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.repo1"
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

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // web flux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Serialization (KotlinX)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    // Jackson Kotlin Module
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // KotlinX + Jackson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-jackson")

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
