plugins {
    id("org.openapi.generator") version "7.3.0"
    id("org.springframework.boot") version "3.2.3" apply false
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "dev.oddsystems.microservices"
version = "0.0.1-SNAPSHOT"

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/java"))
        }
    }
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.openApiGenerate)
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/apis/spec/main.spec.yaml")
    apiPackage.set("dev.oddsystems.microservices.books.server.api")
    modelPackage.set("dev.oddsystems.microservices.books.server.model")
    modelNameSuffix.set("DTO")
    configOptions.putAll(
        mapOf(
            Pair("gradleBuildFile", "false"),
            Pair("useSpringBoot3", "true"),
            Pair("documentationProvider", "none"),
            Pair("interfaceOnly", "true"),
            Pair("openApiNullable", "false"),
        )
    )
    generateApiTests.set(false)
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
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
