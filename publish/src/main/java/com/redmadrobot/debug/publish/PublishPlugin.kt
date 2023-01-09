package com.redmadrobot.debug.publish

//import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.plugins.signing.SigningExtension
import org.gradle.plugins.signing.SigningPlugin
import java.io.File
import java.util.*

class PublishPlugin : Plugin<Project> {
    private lateinit var projectDir: File
    private lateinit var rootDir: File

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
        target.plugins.apply(MavenPublishPlugin::class.java)
        projectDir = target.projectDir
        rootDir = target.rootDir

        with(target) {
            extensions.configure<LibraryExtension>("android") {
                publishing {
                    singleVariant("release") {
                        withSourcesJar()
//                        withJavadocJar()
                    }
                }
            }
            afterEvaluate {
                configurePublishing()
                configureSigning()
            }
        }
    }

    private fun Project.configurePublishing() {
        val libDescription = this.description.orEmpty()

        configure<PublishingExtension> {

            publications.create<MavenPublication>("release") {
                from(components["release"])

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

                        val publishCredentials: PublishCredentials by lazy {
                            try {
                                val localProperties = Properties().apply {
                                    load(File("local.properties").inputStream())
                                }
                                PublishCredentials(
                                    requireNotNull(localProperties.getProperty("OSSRH_USER")),
                                    requireNotNull(localProperties.getProperty("OSSRH_PASSWORD"))
                                )
                            } catch (e: Exception) {
                                // load credentials from system environments if they don't exist in local.properties
                                PublishCredentials(System.getenv("OSSRH_USER"), System.getenv("OSSRH_PASSWORD"))
                            }
                        }

                        credentials {
                            username = publishCredentials.user
                            password = publishCredentials.pass
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

data class PublishCredentials(val user: String?, val pass: String?)