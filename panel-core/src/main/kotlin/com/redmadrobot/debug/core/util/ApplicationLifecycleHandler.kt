package com.redmadrobot.debug.core.util

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

internal class ApplicationLifecycleHandler(
    private val application: Application,
) {
    // open Activity counter
    private val openActivityCounter = AtomicInteger(0)

    private var debugPanelBroadcastReceiver: BroadcastReceiver? = null
    private val debugPanelNotification = DebugPanelNotification(application.applicationContext)

    fun start() {
        registerActivityLifecycleCallback()
    }

    private fun registerActivityLifecycleCallback() {
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityResumed(activity: Activity) {
                    if (openActivityCounter.getAndIncrement() == 0) onAppResumed()

                    // register BroadcastReceiver for debug panel inner actions
                    debugPanelBroadcastReceiver = DebugPanelBroadcastReceiver(activity)
                    val filter = IntentFilter(DebugPanelBroadcastReceiver.ACTION_OPEN_DEBUG_PANEL)

                    ContextCompat.registerReceiver(
                        activity,
                        debugPanelBroadcastReceiver,
                        filter,
                        ContextCompat.RECEIVER_NOT_EXPORTED
                    )
                }

                override fun onActivityPaused(activity: Activity) {
                    (activity as? ComponentActivity)?.let {
                        activity.unregisterReceiver(debugPanelBroadcastReceiver)
                    }
                    if (openActivityCounter.decrementAndGet() == 0) onAppPaused()
                }
            }
        )
    }

    private fun onAppPaused() {
        debugPanelNotification.hide()
    }

    private fun onAppResumed() {
        debugPanelNotification.show()
        initLoggerIfNeeded()
    }

    private fun initLoggerIfNeeded() {
        if (Timber.forest().isEmpty()) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
