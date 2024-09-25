plugins {
    id("convention.debug.panel.plugin")
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin that helps to change the values of shared preferences"

android {
    namespace = "com.redmadrobot.debug.plugin.appsettings"
}
