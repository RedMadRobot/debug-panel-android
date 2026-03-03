package internal

import org.gradle.accessors.dm.LibrariesForAndroidx
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

internal val Project.androidx: LibrariesForAndroidx
    get() = the<LibrariesForAndroidx>()