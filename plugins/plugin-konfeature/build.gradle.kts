plugins {
    id("convention.debug.panel.plugin")
}

description = "Plugin for konfeature library integration"

android {
    namespace = "com.redmadrobot.debug.plugin.konfeature"
}

dependencies {
    implementation(rmr.konfeature)
    implementation(androidx.core)
    implementation(androidx.lifecycle.runtime)
    implementation(stack.timber)
}
