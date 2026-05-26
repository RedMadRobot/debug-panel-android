package com.redmadrobot.debug.plugin.aboutapp

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppAction
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppScreen

/**
 * Plugin that displays information about the application in the debug panel.
 *
 * Accepts an arbitrary list of "title -- value" pairs which will be shown as a list,
 * plus an optional list of custom action buttons rendered below the info section.
 *
 * @param appInfoList list of information entries to display.
 * @param actions list of custom action buttons rendered below the info section.
 * @throws IllegalStateException if both [appInfoList] and [actions] are empty
 *
 * @see AboutAppInfo
 * @see AboutAppAction
 */
public class AboutAppPlugin(
    private val appInfoList: List<AboutAppInfo>,
    private val actions: List<AboutAppAction> = emptyList(),
) : Plugin() {
    init {
        check(appInfoList.isNotEmpty() || actions.isNotEmpty()) {
            "AboutAppPlugin can't be initialized. " +
                "At least one information block or one action must be set."
        }
    }

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AboutAppPluginContainer(
            appInfoList = appInfoList,
            actions = actions,
            commonContainer = commonContainer,
            pushEvent = ::pushEvent,
        )
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
