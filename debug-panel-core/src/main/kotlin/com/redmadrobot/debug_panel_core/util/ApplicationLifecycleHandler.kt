package com.redmadrobot.debug_panel_core.util

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import com.redmadrobot.debug_panel_core.inapp.shake.ShakeController

internal class ApplicationLifecycleHandler(private val application: Application) {

    //Счетчик открытых activity
    private var openActivityCount = 0
    private var notificationManager: NotificationManagerCompat? = null

    private val shakeController = ShakeController(application.applicationContext)
    private val debugPanelNotification = DebugPanelNotification(application.applicationContext)
    private var debugPanelBroadcastReceiver: BroadcastReceiver? = null

    fun start() {
        registerActivityLifecycleCallback()
    }

    private fun registerActivityLifecycleCallback() {
        application.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityResumed(activity: Activity) {
                    if (openActivityCount == 0) onAppResumed()
                    ++openActivityCount

                    (activity as? FragmentActivity)?.let { fragmentActivity ->
                        shakeController.register(fragmentActivity.supportFragmentManager)
                        debugPanelBroadcastReceiver = DebugPanelBroadcastReceiver(
                            fragmentActivity.supportFragmentManager
                        )
                        /*register BroadcastReceiver*/
                        val filter =
                            IntentFilter(DebugPanelBroadcastReceiver.ACTION_OPEN_DEBUG_PANEL)
                        activity.registerReceiver(debugPanelBroadcastReceiver, filter)
                    }
                }

                override fun onActivityPaused(activity: Activity) {
                    --openActivityCount

                    activity.unregisterReceiver(debugPanelBroadcastReceiver)

                    if (openActivityCount == 0) onAppPaused()
                }
            }
        )
    }

    private fun onAppPaused() {
        debugPanelNotification.hide()
    }

    private fun onAppResumed() {
        showDebugNotification()
        shakeController.unregister()
    }

    private fun showDebugNotification() {
        debugPanelNotification.show()
    }
}
