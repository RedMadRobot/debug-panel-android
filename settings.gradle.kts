include(":debug-panel-common")
include(":debug-panel-no-op")
include(
    ":sample",
    ":debug-panel-core",
    ":accounts-plugin",
    ":servers-plugin",
    ":feature-toggles-plugin",
    ":app-settings-plugin",
    ":flipper-plugin",
    ":variable-plugin"
)

/*Plugin aliases*/
project(":accounts-plugin").projectDir =  File (rootDir, "plugins/accounts-plugin")
project(":servers-plugin").projectDir =  File (rootDir, "plugins/servers-plugin")
project(":feature-toggles-plugin").projectDir =  File (rootDir, "plugins/feature-toggles-plugin")
project(":app-settings-plugin").projectDir =  File (rootDir, "plugins/app-settings-plugin")
project(":flipper-plugin").projectDir =  File (rootDir, "plugins/flipper-plugin")
project(":variable-plugin").projectDir =  File (rootDir, "plugins/variable-plugin")
