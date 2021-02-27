package com.redmadrobot.debug_panel_core.util

import android.app.Activity
import android.app.Application
import android.os.Bundle

internal open class ActivityLifecycleCallbacksAdapter: Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {}

    override fun onActivityResumed(activity: Activity) {}
}
