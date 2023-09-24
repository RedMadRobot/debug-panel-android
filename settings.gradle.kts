rootProject.name = "Debug panel"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    includeBuild("publish")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        val version = "2023.05.18"
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
//Base modules
include(
    ":common",
    ":no-op",
    ":core"
)
//Plugins
include(
    ":plugins:accounts",
    ":plugins:servers",
    ":plugins:app-settings",
    ":plugins:flipper",
    ":plugins:variable"
)

include(":sample")
