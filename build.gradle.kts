plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
}

group = "org.summer.story"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}