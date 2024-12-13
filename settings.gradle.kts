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
        val version = "2024.12.12" // Keep it in sync with buildSrc/settings.gradle.kts
        create("rmr") {
            from("com.redmadrobot.versions:versions-redmadrobot:$version")
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
    ":panel-common",
    ":panel-no-op",
    ":panel-core"
)

// Plugins
include(
    ":plugins:plugin-accounts",
    ":plugins:plugin-servers",
    ":plugins:plugin-app-settings",
    ":plugins:plugin-flipper",
    ":plugins:plugin-konfeature",
)

include(":sample")
