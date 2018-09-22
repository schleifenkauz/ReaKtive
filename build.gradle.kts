import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.dokka.gradle.DokkaTask
import org.gradle.api.tasks.bundling.Jar

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    }
}

plugins {
    java
    kotlin("jvm") version "1.2.70"
}

apply {
    plugin("org.jetbrains.dokka")
}

group = "org.nikok"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputDirectory = "docs"
        jdkVersion = 8
        includeNonPublic = false
        noStdlibLink = false
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.nikok:kref:1.0.0") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.jetbrains.spek:spek-api:1.1.5") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.jetbrains.spek:spek-junit-platform-engine:1.1.5") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("com.natpryce:hamkrest:1.5.0.0")
    implementation(kotlin("reflect:1.2.70"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

