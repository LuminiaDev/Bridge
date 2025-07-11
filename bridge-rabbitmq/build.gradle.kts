plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":bridge-common"))
    api("com.rabbitmq:amqp-client:5.25.0")
}