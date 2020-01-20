package com.redmadrobot.debug_panel

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug_panel.inapp.DebugBottomSheet

object DebugPanelController {
    private var fragmentManager: FragmentManager? = null

    fun setActivity(activity: Activity?) {
        fragmentManager = (activity as? FragmentActivity)?.supportFragmentManager
    }

    fun openDebugPanel() {
        fragmentManager?.let { DebugBottomSheet.show(it) }
    }
}
