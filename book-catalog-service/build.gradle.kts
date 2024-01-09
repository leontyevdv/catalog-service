import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "dev.oddsystems.microservices"
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":apis:server"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    builder = "paketobuildpacks/builder:tiny"
}

tasks.withType<JavaCompile> {
    inputs.files(tasks.named("processResources"))
}

tasks.withType<BootRun> {
    systemProperty("spring.profiles.active", "local")
}

task("printSourceSetInformation") {
    doLast {
        sourceSets.forEach { srcSet ->
            println("[" + srcSet.name + "]")
            println("-->Source directories: " + srcSet.allJava.srcDirs)
            println("-->Output directories: " + srcSet.output.classesDirs.files)
            println("-->Compile classpath:")
            srcSet.compileClasspath.files.forEach {
                println("  " + it.path)
            }

            println()
        }
    }
}