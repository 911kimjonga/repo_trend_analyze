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
    // 공통 모듈
    implementation(project(":common"))

    // spring boot starter
    implementation("org.springframework.boot:spring-boot-starter")

    // spring boot web
    implementation("org.springframework.boot:spring-boot-starter-web")

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

    // Serialization (KotlinX)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    // Jackson Kotlin Module
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

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
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
