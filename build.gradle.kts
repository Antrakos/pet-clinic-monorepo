plugins {
    id("java")
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.1"
}

group = "com.github.antrakos.petclinic"
version = "1.0-SNAPSHOT"
allprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }
    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.1"))
    }

    tasks.test {
        useJUnitPlatform()
    }
}
