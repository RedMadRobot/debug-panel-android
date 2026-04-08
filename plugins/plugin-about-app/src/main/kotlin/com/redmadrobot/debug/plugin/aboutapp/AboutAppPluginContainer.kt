package com.redmadrobot.debug.plugin.aboutapp

import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppViewModel
import com.redmadrobot.debug.plugin.aboutapp.utils.ClipboardProvider

internal class AboutAppPluginContainer(
    private val appInfoList: List<AboutAppInfo>,
    private val commonContainer: CommonContainer
) : PluginDependencyContainer {
    fun createAboutAppViewModel(): AboutAppViewModel {
        val clipboardProvider = ClipboardProvider(context = commonContainer.context)

        return AboutAppViewModel(appInfoList = appInfoList, clipboardProvider = clipboardProvider)
    }
}
