plugins {
    alias(stack.plugins.ksp)
    id("convention.debug.panel.plugin")
    alias(stack.plugins.poko)
}

description = "Plugin for showing about app information"

android {
    namespace = "com.redmadrobot.debug.plugin.aboutapp"
}

dependencies {
    implementation(androidx.compose.ui.tooling)
    implementation(androidx.compose.ui.tooling.preview)
}