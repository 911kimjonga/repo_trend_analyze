plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // logging
    implementation("org.springframework.boot:spring-boot-starter-logging")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // kotlin serializable
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
}