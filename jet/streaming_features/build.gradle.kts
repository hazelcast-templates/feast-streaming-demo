plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.hazelcast"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.hazelcast:hazelcast:5.4.0")
    compileOnly("com.hazelcast.jet:hazelcast-jet-kafka:5.4.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fasterxml.jackson:jackson-base:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
    implementation("org.slf4j:slf4j-api:2.0.13")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.Main"
    }
}
