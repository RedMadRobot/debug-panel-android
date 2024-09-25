package internal

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.android(configure: CommonExtension<*, *, *, *, *, *>.() -> Unit) {
    extensions.configure("android", configure)
}
