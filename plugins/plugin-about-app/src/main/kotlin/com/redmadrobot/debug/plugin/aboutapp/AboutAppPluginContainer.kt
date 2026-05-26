package com.redmadrobot.debug.plugin.aboutapp

import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppAction
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppViewModel
import com.redmadrobot.debug.plugin.aboutapp.utils.ClipboardProvider

internal class AboutAppPluginContainer(
    private val appInfoList: List<AboutAppInfo>,
    private val actions: List<AboutAppAction>,
    private val commonContainer: CommonContainer,
    private val pushEvent: (DebugEvent) -> Unit,
) : PluginDependencyContainer {
    fun createAboutAppViewModel(): AboutAppViewModel {
        val clipboardProvider = ClipboardProvider(context = commonContainer.context)

        return AboutAppViewModel(
            appInfoList = appInfoList,
            actions = actions,
            context = commonContainer.context,
            clipboardProvider = clipboardProvider,
            pushEvent = pushEvent,
        )
    }
}
