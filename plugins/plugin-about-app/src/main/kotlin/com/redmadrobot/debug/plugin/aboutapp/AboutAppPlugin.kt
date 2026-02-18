package com.redmadrobot.debug.plugin.aboutapp

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.EditablePlugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppScreen

public class AboutAppPlugin(
    private val aboutAppInfo: List<AboutAppInfo>
) : Plugin(), EditablePlugin {
    init {
        aboutAppInfo.firstOrNull()
            ?: error("AboutAppPlugin can't be initialized. At least one information block must be set.")
    }

    internal companion object {
        internal const val NAME = "ABOUT APP"
    }

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AboutAppPluginContainer(aboutAppInfo = aboutAppInfo)
    }

    override fun getName(): String = NAME

    @Composable
    override fun content() {
        AboutAppScreen()
    }
}
