package com.redmadrobot.debug.core.ui.debugpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.inapp.compose.DebugPanelScreen
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import kotlinx.coroutines.launch

internal class DebugPanelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val debugPanel = remember { DebugPanel.getInstance()!! }
            val themeMode by debugPanel.observeDebugPanelTheme()
                .collectAsStateWithLifecycle(initialValue = debugPanel.getSelectedTheme())

            DebugPanelTheme(themeMode = themeMode) {
                DebugPanelScreen(
                    themeMode = themeMode,
                    onThemeModeChange = { mode ->
                        lifecycleScope.launch { debugPanel.updateDebugPanelTheme(themeMode = mode) }
                    },
                    onClose = { finish() },
                )
            }
        }
    }
}
