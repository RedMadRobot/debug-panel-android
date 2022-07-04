object Plugins {

    private const val ANDROID_PLUGIN = "7.0.0"

    object Kotlin {
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val androidPlugin = "android"
        const val androidExtensions = "android.extensions"
        const val kapt = "kapt"
    }

    object Android {
        const val gradlePlugin = "com.android.tools.build:gradle:$ANDROID_PLUGIN"
        const val applicationPlugin = "com.android.application"
        const val libraryPlagin = "com.android.library"
    }
}