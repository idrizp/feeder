plugins {
    id("java")
}

group = "dev.idriz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("io.javalin:javalin:6.4.0")
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.test {
    dependsOn(project(":common").tasks.test)
}