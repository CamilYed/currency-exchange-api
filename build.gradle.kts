plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "camilyed.github.io"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Boot Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("io.strikt:strikt-core:0.34.0")

    // Integration Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    testImplementation("org.testcontainers:postgresql:1.20.2")
    testImplementation("org.flywaydb:flyway-core")
    testRuntimeOnly("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

val integrationTest: SourceSet =
    sourceSets.create("integrationTest") {
        java {
            compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
            runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
            srcDir("src/integration-test/java")
        }
        resources.srcDir("src/integration-test/resources")
    }

configurations[integrationTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[integrationTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())

val integrationTestTask =
    tasks.register<Test>("integrationTest") {
        group = "verification"

        useJUnitPlatform()

        testClassesDirs = integrationTest.output.classesDirs
        classpath = sourceSets["integrationTest"].runtimeClasspath

        shouldRunAfter("test")
    }

tasks.check {
    dependsOn(integrationTestTask)
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "io.gitlab.arturbosch.detekt")
