package internal

import org.gradle.accessors.dm.LibrariesForAndroidx
import org.gradle.accessors.dm.LibrariesForStack
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

internal val Project.androidx: LibrariesForAndroidx
    get() = the<LibrariesForAndroidx>()

internal val Project.stack: LibrariesForStack
    get() = the<LibrariesForStack>()