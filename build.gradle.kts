plugins {
    id("java")
}

group = "dev.martim.marketplace"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("org.mongodb:mongodb-driver-sync:5.1.0")
    implementation("com.google.code.gson:gson:2.11.0")

    compileOnly(files("lib/spigot-1.20.4-R0.1-SNAPSHOT.jar"))
}

tasks.register<Copy>("copyJar") {
    dependsOn("jar")

    from("build/libs/marketplace-1.0-SNAPSHOT.jar")
    into("server/plugins")
}

tasks.jar {
    exclude("META-INF/*.RSA", "META-INF/*.SF'", "META-INF/*.DSA")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}