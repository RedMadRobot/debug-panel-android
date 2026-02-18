plugins {
    id("convention.debug.panel.plugin")
    alias(stack.plugins.ksp)
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin for showing about app information"

android {
    namespace = "com.redmadrobot.debug.plugin.aboutapp"
}
