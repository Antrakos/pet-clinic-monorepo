import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0-SNAPSHOT" apply false
    id("io.spring.dependency-management") version "1.1.1"
}

val GITHUB_REF_NAME = System.getenv("GITHUB_REF_NAME")
val GITHUB_SHA = System.getenv("GITHUB_SHA")
val DOCKER_USERNAME = System.getenv("DOCKER_USERNAME")
val DOCKER_PASSWORD= System.getenv("DOCKER_PASSWORD")

val services = project("services").subprojects

allprojects {
    apply(plugin = "java")

    group = "com.github.antrakos.petclinic"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

configure(services) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    version = "1.0"

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.0-SNAPSHOT"))
    }

    fun version() = when(GITHUB_REF_NAME) {
        "master" -> "$version-${GITHUB_SHA?.take(6)}"
        null -> "$version-local"
        else -> "$version-$GITHUB_REF_NAME-${GITHUB_SHA?.take(6)}"
    }

    tasks.withType<BootBuildImage> {
        imageName = "antrakos/${project.name}:${version()}"
        if (GITHUB_REF_NAME == "master") tags.add("antrakos/${project.name}:latest")
        environment.put("BPE_DELIM_JAVA_TOOL_OPTIONS", " ")
        buildpacks.set(listOf("paketobuildpacks/adoptium:latest", "paketo-buildpacks/java"))
        docker {
            builderRegistry {
                username = DOCKER_USERNAME
                password = DOCKER_PASSWORD
            }
        }
    }
}
