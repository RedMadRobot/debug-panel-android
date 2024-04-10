import internal.android
import java.util.Properties

plugins {
    id("maven-publish")
    id("signing")
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

private val publishProperties by lazy {
    rootProject.file("gradle/publish.properties").inputStream()
        .use { Properties().apply { load(it) } }
}
val libraryName by lazy {
    val properties = project.file("library.properties").inputStream()
        .use { Properties().apply { load(it) } }
    properties.getProperty("lib_name")
}

publishing {
    publications.create<MavenPublication>("release") {
        artifactId = libraryName

        afterEvaluate { from(components["release"]) }

        pom {
            name = libraryName
            description = provider { project.description }
            url = publishProperties.getProperty("home_page")

            licenses {
                license {
                    name = publishProperties.getProperty("license_name")
                    url = publishProperties.getProperty("license_url")
                }
            }

            developers {
                developer {
                    id = "Zestxx"
                    name = "Roman Choriev"
                    email = "r.choryev@redmadrobot.com"
                }
            }

            contributors {
                contributor {
                    name = "Osip Fatkullin"
                    email = "o.fatkullin@redmadrobot.com"
                }
                contributor {
                    name = "Alexandr Anisimov"
                    email = "PilotOfSparrow@gmail.com"
                }
                contributor {
                    name = "Dmitry trabo"
                    email = "dtrabo@gmail.com"
                }
            }

            scm {
                connection = "scm:git:git://github.com/RedMadRobot/debug-panel-android.git"
                developerConnection = "scm:git:ssh://github.com/RedMadRobot/debug-panel-android.git"
                url = publishProperties.getProperty("home_page")
            }
        }
    }

    repositories {
        mavenLocal()
        maven {
            name = "OSSRH"
            url = project.uri(publishProperties.getProperty("sonatype_repo"))

            credentials {
                username = System.getenv("OSSRH_USER")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

afterEvaluate {
    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
    }
}
