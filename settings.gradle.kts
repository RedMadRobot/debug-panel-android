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
    ":debug-panel-common",
    ":debug-panel-no-op",
    ":debug-panel-core"
)
//Plugins
include(
    ":plugins:accounts-plugin",
    ":plugins:servers-plugin",
    ":plugins:app-settings-plugin",
    ":plugins:flipper-plugin",
    ":plugins:variable-plugin"
)

include(":sample")
