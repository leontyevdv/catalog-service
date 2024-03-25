import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.2.4"
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

dependencies {
    implementation("org.springframework.cloud:spring-cloud-config-server")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
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
