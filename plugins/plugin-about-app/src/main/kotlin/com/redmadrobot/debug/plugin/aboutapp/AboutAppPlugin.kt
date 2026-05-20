package com.redmadrobot.debug.plugin.aboutapp

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppScreen

/**
 * Plugin that displays information about the application in the debug panel.
 *
 * Accepts an arbitrary list of "title -- value" pairs
 * which will be shown as a list.
 *
 * @param appInfoList list of information entries to display.
 *   Must contain at least one element.
 * @throws IllegalStateException if [appInfoList] is empty
 *
 * @see AboutAppInfo
 */
public class AboutAppPlugin(
    private val appInfoList: List<AboutAppInfo>
) : Plugin() {
    init {
        appInfoList.firstOrNull()
            ?: error("AboutAppPlugin can't be initialized. At least one information block must be set.")
    }

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AboutAppPluginContainer(appInfoList = appInfoList, commonContainer = commonContainer)
    }

    override fun getName(): String = NAME

    @Composable
    override fun content() {
        AboutAppScreen()
    }

    internal companion object {
        internal const val NAME = "ABOUT APP"
    }
}
