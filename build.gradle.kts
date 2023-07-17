plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0-SNAPSHOT"
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
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.0-SNAPSHOT"))
    }

    tasks.test {
        useJUnitPlatform()
    }
}
