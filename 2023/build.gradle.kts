plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.AMAZON
    }
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }
}
