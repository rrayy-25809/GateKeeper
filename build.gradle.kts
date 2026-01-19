plugins {
    java
    id("com.gradleup.shadow") version "9.0.0" // 최신 버전(9.x 이상) 사용
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

group = "com.rrayy"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://buf.build/gen/maven") // Protocol Buffers
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://oss.sonatype.org/content/repositories/snapshots/") // BungeeCord Chat API
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT") // Paper API
    implementation("build.buf.gen:minekube_gate_protocolbuffers_java:33.4.0.1.20250516132630.2a0c7768e191")

    // ⚠️ 중요: gRPC runtime 직접 포함, 무조건 shadowJar로 빌드해야 함
    implementation("build.buf.gen:minekube_gate_grpc_java:1.78.0.1.20250516132630.2a0c7768e191")
    implementation("io.grpc:grpc-netty-shaded:1.68.1")
    implementation("io.grpc:grpc-protobuf:1.68.1")
    implementation("io.grpc:grpc-stub:1.68.1")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar { // Jar 작업 비활성화
    enabled = false
}

tasks.shadowJar {
    relocate(
        "com.google.protobuf",
        "com.rrayy.gatekeeper.shadow.protobuf"
    )
    relocate(
        "io.grpc",
        "com.rrayy.gatekeeper.shadow.grpc"
    )
    relocate(
        "build.buf.gen.minekube",
        "com.rrayy.gatekeeper.shadow.minekube"
    )

    archiveClassifier.set("all")

    // META-INF 충돌 방지
    mergeServiceFiles()

    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    exclude("META-INF/INDEX.LIST")
    exclude("META-INF/DEPENDENCIES")

    // Java 9+ multi-release 문제 회피
    exclude("META-INF/versions/**")
}