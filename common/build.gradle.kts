plugins {
    id("java-library")
}

group = "dev.idriz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
    api("org.jetbrains:annotations:26.0.1")
    api("org.apache.kafka:kafka-clients:3.9.0")
    api("io.sentry:sentry:7.19.1")
    api("org.slf4j:slf4j-simple:2.0.16")
    api("com.fasterxml.jackson.core:jackson-databind:2.18.2")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.mockito:mockito-core:5.14.2")
    mockitoAgent("org.mockito:mockito-core:5.14.2") { isTransitive = false }
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}


tasks.test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}