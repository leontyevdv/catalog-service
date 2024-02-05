import io.gatling.gradle.LogHttp

plugins {
    id("java")
    id("io.gatling.gradle") version "3.10.3.2"
}

group = "dev.oddsystems.microservices"
version = "0.0.1-SNAPSHOT"

gatling {
    logLevel = "WARN"
    logHttp = LogHttp.NONE
    // do not include classes and resources from src/main
    includeMainOutput = false
    // do not include classes and resources from src/test
    includeTestOutput = false

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}
