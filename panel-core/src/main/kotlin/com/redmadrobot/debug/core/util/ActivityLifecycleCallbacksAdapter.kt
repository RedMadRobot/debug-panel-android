package com.redmadrobot.debug.core.util

import android.app.Activity
import android.app.Application
import android.os.Bundle

internal open class ActivityLifecycleCallbacksAdapter : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivityCreated(activity: Activity, p1: Bundle?) = Unit

    override fun onActivityResumed(activity: Activity) = Unit
}
