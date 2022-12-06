package com.redmadrobot.debug.core.annotation

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is an internal part of DebugPanel. You shouldn't use it, cause it can be changed in future"
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class DebugPanelInternal
