rootProject.name = "Debug panel"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google {
            content {
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                includeGroupAndSubgroups("androidx")
            }
        }
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                includeGroupAndSubgroups("androidx")
            }
        }

        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        val version = "2026.07.10" // Keep it in sync with buildSrc/settings.gradle.kts
        create("rmr") {
            from("com.redmadrobot.versions:versions-redmadrobot:$version")
            version("konfeature", "1.1.0") // Remove with update version
            library("konfeature-ui", "com.redmadrobot.konfeature:konfeature-ui:1.1.0") // Remove with update version
            library("konfeature-ui-noop", "com.redmadrobot.konfeature:konfeature-ui-noop:1.1.0") // Remove with update version
        }
        create("androidx") {
            from("com.redmadrobot.versions:versions-androidx:$version")
        }
        create("stack") {
            from("com.redmadrobot.versions:versions-stack:$version")
        }
    }
}

// Base modules
include(
    ":panel-no-op",
    ":panel-core",
    ":panel-ui-kit"
)

// Plugins
include(
    ":plugins:plugin-servers",
    ":plugins:plugin-konfeature",
    ":plugins:plugin-about-app",
)

include(":sample")
