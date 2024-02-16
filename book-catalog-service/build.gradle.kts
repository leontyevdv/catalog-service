import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    id("org.springframework.boot") version "3.2.2"
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

extra["springCloudVersion"] = "2023.0.0"
extra["testcontainersVersion"] = "1.19.3"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.flywaydb:flyway-core")

    implementation(project(":apis:server"))

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.testcontainers:postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    environment.set(environment.get() + mapOf("BP_JVM_VERSION" to "21"))
//    imageName.set(project.name)
//    publish.set(true)

//    docker {
//        publishRegistry {
//            url.set(project.findProperty("registryUrl") as String)
//            username.set(project.findProperty("registryUsername") as String)
//            password.set(project.findProperty("registryToken") as String)
//        }
//    }
}

tasks.withType<JavaCompile> {
    inputs.files(tasks.named("processResources"))
}

tasks.withType<BootRun> {
    systemProperty("spring.profiles.active", "prod")
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