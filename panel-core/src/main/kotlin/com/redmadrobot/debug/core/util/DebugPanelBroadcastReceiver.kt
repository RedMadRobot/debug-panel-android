package com.redmadrobot.debug.core.util

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.redmadrobot.debug.core.DebugPanel
import java.lang.ref.WeakReference

internal class DebugPanelBroadcastReceiver(activity: Activity) : BroadcastReceiver() {
    private val activityReference = WeakReference(activity)

    companion object {
        const val ACTION_OPEN_DEBUG_PANEL = "com.redmadrobot.debug.core.ACTION_OPEN_DEBUG_PANEL"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_OPEN_DEBUG_PANEL) {
            activityReference.get()?.let { DebugPanel.showPanel(it) }
        }
    }
}
