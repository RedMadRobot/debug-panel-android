rootProject.name = "Debug panel"
include(":debug-panel-common")
include(":debug-panel-no-op")
include(":sample")
include(":debug-panel-core")

//Plugins
include(
    ":plugins:accounts-plugin",
    ":plugins:servers-plugin",
//    ":plugins:feature-toggles-plugin",
    ":plugins:app-settings-plugin",
    ":plugins:flipper-plugin",
    ":plugins:variable-plugin"
)
