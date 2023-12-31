plugins {
    id("io.freefair.lombok") version "8.1.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")

    testImplementation("org.testcontainers:postgresql")
}
