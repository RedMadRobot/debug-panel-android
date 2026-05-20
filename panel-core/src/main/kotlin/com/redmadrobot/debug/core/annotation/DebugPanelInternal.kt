package com.redmadrobot.debug.core.annotation

/**
 * Marks an API as internal to the debug panel.
 *
 * Entities with this annotation are intended for use only within the library's modules and may change without notice.
 *
 * Using a marked API outside the library will produce a compilation error.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is an internal part of DebugPanel. You shouldn't use it, cause it can be changed in future"
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class DebugPanelInternal
