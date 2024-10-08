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
    maven {
        url = uri("https://repo.spring.io/release")
    }
    maven {
        url = uri("https://repo.spring.io/milestone")
    }
    maven {
        url = uri("https://repo.spring.io/snapshot")
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

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4") // Dodanie BOM dla Spring Cloud
    }
}

dependencies {
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.jetbrains.exposed:exposed-core:0.55.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.55.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.55.0")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("io.github.openfeign:feign-jackson:12.4")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.2")
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("org.testcontainers:postgresql:1.20.2")

    // Spring Boot Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("io.strikt:strikt-core:0.34.0")
    testImplementation("io.strikt:strikt-jackson:0.34.0")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    // Integration Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.wiremock:wiremock:3.9.1")
    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
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

ktlint {
    version.set("0.49.1")
    filter {
        exclude("**/test/**")
        exclude("**/integrationTest/**")
    }
}

tasks.wrapper {
    gradleVersion = "8.10.2"
    distributionType = Wrapper.DistributionType.ALL
}
