package com.redmadrobot.debug.core.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug.core.internal.DebugPanel

internal class DebugPanelBroadcastReceiver(
    private val supportFragmentManager: FragmentManager
) : BroadcastReceiver() {

    companion object {
        const val ACTION_OPEN_DEBUG_PANEL =
            "com.redmadrobot.debug_panel_core.ACTION_OPEN_DEBUG_PANEL"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_OPEN_DEBUG_PANEL) {
            DebugPanel.showPanel(supportFragmentManager)
        }
    }
}
