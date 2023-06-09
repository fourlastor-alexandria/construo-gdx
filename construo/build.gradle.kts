@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    `java-gradle-plugin`
    `maven-publish`
    signing
    alias(libs.plugins.spotless)
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

spotless {
    isEnforceCheck = false
    java {
        palantirJavaFormat()
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

gradlePlugin {
    plugins {
        create("construo") {
            id = "io.github.fourlastor.gdx.construo"
            implementationClass = "io.github.fourlastor.construo.ConstruoPlugin"
        }
    }
}

dependencies {
    implementation("org.beryx:badass-runtime-plugin:1.13.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "construo"
            from(components["java"])
            pom {
                name.set("Construo")
                description.set("A plugin to cross compile libGDX games")
                url.set("https://www.github.com/fourlastor-alexandria/construo-gdx")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.github.com/fourlastor-alexandria/construo-gdx/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("fourlastor")
                        name.set("Daniele Conti")
                    }
                }
                scm {
                    connection.set("scm:git:https://www.github.com/fourlastor-alexandria/construo-gdx.git")
                    developerConnection.set("scm:git:https://www.github.com/fourlastor-alexandria/construo-gdx.git")
                    url.set("https://www.github.com/fourlastor-alexandria/construo-gdx")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
