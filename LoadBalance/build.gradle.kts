plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://plugins.gradle.org/m2/")
    gradlePluginPortal()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}