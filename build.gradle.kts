plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
}

group = "org.summer.story"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // for networking
    implementation("io.netty:netty-all:4.1.119.Final")
    
    // for logging
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.17")

    // for IoC
    implementation("io.insert-koin:koin-core:4.0.2")

    // for configuration
    implementation("com.charleskorn.kaml:kaml:0.72.0")

    // for job scheduling
    implementation("com.github.Pool-Of-Tears:KtScheduler:1.1.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    // for database
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.flywaydb:flyway-core:11.5.0")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:11.5.0")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.17")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs(
        "-XX:+EnableDynamicAgentLoading", // to avoid the "Java 9+ is required to enable dynamic agent loading" warning when using mockk
        "-Djdk.instrument.traceUsage=false", // to avoid showing the internal injection logic when using mockk
        "-Xshare:off" // to avoid the annoying "shared archive is not supported" warning when using mockk
    )
}

kotlin {
    jvmToolchain(21)
}