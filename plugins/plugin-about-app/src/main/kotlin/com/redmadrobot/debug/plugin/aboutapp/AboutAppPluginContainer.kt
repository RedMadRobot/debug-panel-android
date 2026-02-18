package com.redmadrobot.debug.plugin.aboutapp

import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.ui.AboutAppViewModel

internal class AboutAppPluginContainer(
    private val aboutAppInfo: List<AboutAppInfo>
) : PluginDependencyContainer {
    fun createAboutAppViewModel(): AboutAppViewModel {
        return AboutAppViewModel(aboutAppInfo = aboutAppInfo)
    }
}
