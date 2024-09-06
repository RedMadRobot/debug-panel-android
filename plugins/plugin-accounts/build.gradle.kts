plugins {
    id("convention.plugin")
    alias(stack.plugins.ksp)
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin for switching user accounts"

android {
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    namespace = "com.redmadrobot.debug.plugin.accounts"
}

dependencies {
    ksp(androidx.room.compiler)
}
