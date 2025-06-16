plugins {
    id("java")
}

group = "com.luminia.bridge.rabbitmq"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))
    api("com.rabbitmq:amqp-client:5.25.0")
}