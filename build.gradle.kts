import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.google.protobuf") version "0.8.10"
    id("org.jetbrains.gradle.plugin.idea-ext") version "0.7"
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("javax.annotation:javax.annotation-api:1.2")

    implementation(grpc("grpc-protobuf"))
    implementation(grpc("grpc-netty-shaded"))
    implementation(grpc("grpc-stub"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")

}

protobuf {

    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }

    plugins {
        id("grpc") {
            artifact = grpc("protoc-gen-grpc-java")
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach { it.plugins { id("grpc") } }
    }

}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

fun grpc(artifact: String) = "io.grpc:$artifact:1.24.0"

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    version = "5.6.4"
}
