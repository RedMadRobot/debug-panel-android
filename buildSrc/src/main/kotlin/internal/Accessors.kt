package internal

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.android(configure: LibraryExtension.() -> Unit) {
    extensions.configure("android", configure)
}
