rootProject.name = "Debug panel"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

include(":debug-panel-common")
include(":debug-panel-no-op")
include(":sample")
include(":debug-panel-core")

//Plugins
include(
    ":plugins:accounts-plugin",
    ":plugins:servers-plugin",
    ":plugins:app-settings-plugin",
    ":plugins:flipper-plugin",
    ":plugins:variable-plugin"
)
