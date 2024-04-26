package com.redmadrobot.debug.core.util

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.redmadrobot.debug.core.inapp.shake.ShakeController
import timber.log.Timber

internal class ApplicationLifecycleHandler(
    private val application: Application,
    private val shakerMode: Boolean,
) {

    // open Activity counter
    private var openActivityCount = 0

    private var shakeController: ShakeController? = null
    private var debugPanelBroadcastReceiver: BroadcastReceiver? = null
    private val debugPanelNotification = DebugPanelNotification(application.applicationContext)

    fun start() {
        if (shakerMode) {
            shakeController = ShakeController(application.applicationContext)
        }
        registerActivityLifecycleCallback()
    }

    private fun registerActivityLifecycleCallback() {
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityResumed(activity: Activity) {
                    if (openActivityCount == 0) onAppResumed()
                    ++openActivityCount

                    (activity as? FragmentActivity)?.let { fragmentActivity ->
                        shakeController?.register(fragmentActivity.supportFragmentManager)

                        /*register BroadcastReceiver for debug panel inner actions*/
                        debugPanelBroadcastReceiver = DebugPanelBroadcastReceiver(
                            fragmentActivity.supportFragmentManager
                        )
                        val filter = IntentFilter(
                            DebugPanelBroadcastReceiver.ACTION_OPEN_DEBUG_PANEL
                        )

                        ContextCompat.registerReceiver(
                            activity,
                            debugPanelBroadcastReceiver,
                            filter,
                            ContextCompat.RECEIVER_NOT_EXPORTED
                        )
                    }
                }

                override fun onActivityPaused(activity: Activity) {
                    --openActivityCount

                    (activity as? FragmentActivity)?.let {
                        activity.unregisterReceiver(debugPanelBroadcastReceiver)
                    }
                    if (openActivityCount == 0) onAppPaused()
                }
            }
        )
    }

    private fun onAppPaused() {
        debugPanelNotification.hide()
        shakeController?.unregister()
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
