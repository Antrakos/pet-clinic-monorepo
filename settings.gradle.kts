pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "pet-clinic-monorepo"
include("services:customers-service")
include("services:vets-service")
include("services:visits-service")
