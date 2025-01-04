plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "dev.idriz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
    implementation(project(":common"))
    implementation("io.prometheus:prometheus-metrics-core:1.3.5")
    implementation("io.prometheus:prometheus-metrics-exporter-httpserver:1.3.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.mockito:mockito-core:5.14.2")
    mockitoAgent("org.mockito:mockito-core:5.14.2") { isTransitive = false }
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "dev.idriz.feeder.prometheus.Application"
        )
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}