plugins {
    id("java")
}

group = "ru.ikar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("com.google.code.gson:gson:2.8.2")
}

