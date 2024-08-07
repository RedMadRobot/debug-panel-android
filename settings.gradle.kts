rootProject.name = "Debug panel"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }

    versionCatalogs {
        val version = "2024.04.10"
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
    ":common",
    ":no-op",
    ":core"
)

// Plugins
include(
    ":plugins:accounts",
    ":plugins:servers",
    ":plugins:app-settings",
    ":plugins:flipper",
    ":plugins:variable",
    ":plugins:konfeature",
)

include(":sample")
