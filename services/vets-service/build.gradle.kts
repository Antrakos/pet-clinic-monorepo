dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    testImplementation("org.testcontainers:mongodb")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

tasks.test {
    jvmArgs(listOf("--enable-preview"))
}

tasks.bootBuildImage {
    environment.put("BPE_APPEND_JAVA_TOOL_OPTIONS", "--enable-preview")
}
