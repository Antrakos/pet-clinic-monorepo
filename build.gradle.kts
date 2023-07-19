plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.1"
}

val GITHUB_REF_NAME: String? by project

val services = project("services").subprojects

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "com.github.antrakos.petclinic"

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

configure(services) {
    version = if (GITHUB_REF_NAME != "master") "1.0-${GITHUB_REF_NAME ?: "local"}" else "1.0"
    tasks.bootBuildImage {
        imageName = "antrakos/${project.name}:${project.version}"
        if (GITHUB_REF_NAME == "master") tags.add("antrakos/${project.name}:latest")
        environment.put("BPE_DELIM_JAVA_TOOL_OPTIONS", " ")
        buildpacks.set(listOf("paketobuildpacks/adoptium:latest", "paketo-buildpacks/java"))
    }
}
