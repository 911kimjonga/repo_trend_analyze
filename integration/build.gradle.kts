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

    sourceSets.all {
        languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // common module
    implementation(project(":common"))

    // spring boot starter
    implementation("org.springframework.boot:spring-boot-starter")

    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // kotlin serializable
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    // web flux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.113.Final:osx-aarch_64")

    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

    // tester
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.projectreactor:reactor-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}