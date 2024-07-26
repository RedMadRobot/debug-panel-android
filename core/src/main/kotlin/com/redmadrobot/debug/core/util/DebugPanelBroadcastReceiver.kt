package com.redmadrobot.debug.core.util

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.redmadrobot.debug.core.DebugPanel

internal class DebugPanelBroadcastReceiver(
    private val activity: Activity
) : BroadcastReceiver() {

    companion object {
        const val ACTION_OPEN_DEBUG_PANEL = "com.redmadrobot.debug.core.ACTION_OPEN_DEBUG_PANEL"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_OPEN_DEBUG_PANEL) {
            DebugPanel.showPanel(activity)
        }
    }
}
