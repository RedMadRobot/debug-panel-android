package com.redmadrobot.build

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.io.File
import java.util.*

class PublishPlugin : Plugin<Project> {
    lateinit var projectDir: File
    lateinit var rootDir: File

    private val publishProperties by lazy {

        Properties().apply {
            load(File("$rootDir/gradle/publish.properties").inputStream())
        }
    }

    private val libraryProperties by lazy {
        Properties().apply {
            load(File("$projectDir/library.properties").inputStream())
        }
    }

    override fun apply(target: Project) {
        projectDir = target.projectDir
        rootDir = target.rootDir

        with(target) {
            afterEvaluate {
                configurePublishing()
                configureSigning()
            }
        }
    }

    private fun Project.configurePublishing() {
        plugins.apply(MavenPublishPlugin::class.java)
        val libDescription = this.description.orEmpty()

        configure<PublishingExtension> {
            val android = extensions.getByName<BaseExtension>("android")

            val sourcesJar = tasks.register<Jar>("sourcesJar") {
                archiveClassifier.set("sources")
                from(android.sourceSets["main"].java.srcDirs)
            }

            publications {
                create("release", MavenPublication::class.java) {
                    from(project.components.findByName("release"))

                    artifact(sourcesJar)

                    groupId = publishProperties.getProperty("group_id")
                    artifactId = libraryProperties.getProperty("lib_name")
                    version = publishProperties.getProperty("lib_version")

                    pom {
                        name.set(libraryProperties.getProperty("lib_name"))
                        description.set(libDescription)
                        url.set(publishProperties.getProperty("home_page"))

                        licenses {
                            license {
                                name.set(publishProperties.getProperty("license_name"))
                                url.set(publishProperties.getProperty("license_url"))
                            }
                        }

                        developers {
                            developer {
                                id.set("Zestxx")
                                name.set("Roman Choriev")
                                email.set("r.choryev@redmadrobot.com")
                            }
                        }

                        contributors {
                            contributor {
                                name.set("Osip Fatkullin")
                                email.set("o.fatkullin@redmadrobot.com")
                            }
                            contributor {
                                name.set("Alexandr Anisimov")
                                email.set("PilotOfSparrow@gmail.com")
                            }
                            contributor {
                                name.set("Dmitry trabo")
                                email.set("dtrabo@gmail.com")
                            }
                        }

                        scm {
                            connection.set("scm:git:git://github.com/RedMadRobot/debug-panel-android.git")
                            developerConnection.set("scm:git:ssh://github.com/RedMadRobot/debug-panel-android.git")
                            url.set(publishProperties.getProperty("home_page"))
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
            }
        }
    }

    private fun Project.configureSigning() {
        plugins.apply(SigningPlugin::class.java)

        configure<SigningExtension> {
            val publishing = extensions.getByType(PublishingExtension::class.java)

            useGpgCmd()
            sign(publishing.publications.getByName("release"))
        }
    }
}
