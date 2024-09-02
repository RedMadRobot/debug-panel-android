package com.redmadrobot.debug.core.ui.debugpanel

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.core.view.WindowCompat
import com.redmadrobot.debug.core.inapp.compose.DebugBottomSheet

internal class DebugBottomSheetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureWindow()

        setContent {
            MaterialTheme {
                DebugBottomSheet(onClose = { this.finish() })
            }
        }
    }

    private fun configureWindow() = with(window) {
        WindowCompat.setDecorFitsSystemWindows(this, false)
        this.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}