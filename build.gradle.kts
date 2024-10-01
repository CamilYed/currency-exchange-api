plugins {
    kotlin("jvm") version "2.0.20"
    id("io.gitlab.arturbosch.detekt") version("1.23.7")
}

group = "camilyed.github.io"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}