package com.redmadrobot.debug.core.ui.debugpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.redmadrobot.debug.core.inapp.compose.DebugPanelScreen

internal class DebugPanelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DebugPanelScreen(onClose = { finish() })
            }
        }
    }
}
