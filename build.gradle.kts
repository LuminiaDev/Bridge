plugins {
    `java-library`
    id("java")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

allprojects {
    group = "com.luminia"
    version = "1.0.6-SNAPSHOT"
}

subprojects {
    apply {
        plugin("java-library")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        api("org.jetbrains:annotations:26.0.2")
        api("org.projectlombok:lombok:1.18.36")
        annotationProcessor("org.projectlombok:lombok:1.18.36")
    }
}