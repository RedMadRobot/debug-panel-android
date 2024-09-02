import com.redmadrobot.build.dsl.*
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()

    pom {
        name.convention(project.name)
        description.convention(project.description)

        licenses {
            mit()
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

        setGitHubProject("RedMadRobot/debug-panel-android")
    }
}

publishing {
    repositories {
        if (isRunningOnCi) githubPackages("RedMadRobot/debug-panel-android")
    }
}
