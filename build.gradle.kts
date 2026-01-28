plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
    id("io.freefair.lombok") version "8.4"
}

group = "org.mdafftfa"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.powernukkitx.org/releases")
    maven("https://jitpack.io")
    maven("https://repo.opencollab.dev/maven-releases")
    maven("https://repo.opencollab.dev/maven-snapshots")
}

dependencies {
    compileOnly("org.powernukkitx:server:2.0.0-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "org.mdafftfa"
            artifactId = "slapper"
            version = "1.0-SNAPSHOT"
            artifact(tasks.named("shadowJar").get()) {
                classifier = "latest"
            }
            repositories.maven("/tmp/test-repo")
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveFileName.set("Slapper.jar")
    destinationDirectory.set(file("../../"))
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        from("src/main/resources") {
            include("lang/**")
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    build {
        dependsOn(shadowJar)
        dependsOn("publishToMavenLocal")
    }
}

