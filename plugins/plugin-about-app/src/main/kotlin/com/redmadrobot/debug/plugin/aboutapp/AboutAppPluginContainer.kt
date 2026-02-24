package com.redmadrobot.debug.plugin.aboutapp

import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppViewModel

internal class AboutAppPluginContainer(
    private val appInfoList: List<AboutAppInfo>
) : PluginDependencyContainer {
    fun createAboutAppViewModel(): AboutAppViewModel {
        return AboutAppViewModel(appInfoList = appInfoList)
    }
}
