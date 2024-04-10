package internal

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.plugins.ExtensionAware

internal fun ExtensionAware.android(configure: LibraryExtension.() -> Unit) {
    extensions.configure<LibraryExtension>("android", configure)
}
