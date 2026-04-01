import internal.androidx

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    "implementation"(androidx.compose.foundation)
    "implementation"(androidx.compose.material)
    "implementation"(androidx.compose.material3)
    "implementation"(androidx.compose.ui)
    "implementation"(androidx.compose.runtime)
    "implementation"(androidx.lifecycle.viewmodel.compose)
}
