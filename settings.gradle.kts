rootProject.name = "Debug panel"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {

    repositories {
        google()
        mavenCentral()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        val version = "2022.09.13"
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
    ":plugins:servers-plugin",
    ":plugins:app-settings",
    ":plugins:flipper",
    ":plugins:variable-plugin"
)

include(":sample")
