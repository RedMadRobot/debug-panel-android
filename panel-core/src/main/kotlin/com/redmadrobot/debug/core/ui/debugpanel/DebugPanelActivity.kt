package com.redmadrobot.debug.core.ui.debugpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.redmadrobot.debug.core.inapp.compose.DebugPanelScreen
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.debug.uikit.theme.ThemeState

internal class DebugPanelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState = remember { ThemeState() }
            DebugPanelTheme(themeState = themeState) {
                DebugPanelScreen(onClose = { finish() })
            }
        }
    }
}
