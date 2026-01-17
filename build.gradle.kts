plugins {
    java
}

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
    implementation("build.buf.gen:minekube_gate_protocolbuffers_java:28.3.0.2.20241118150055.50fffb007499")
    implementation("build.buf.gen:minekube_gate_grpc_java:1.68.1.1.20241118150055.50fffb007499")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java { // 자바 버전 설정
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}