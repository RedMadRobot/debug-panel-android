package com.redmadrobot.debug.plugin.aboutapp.ui

import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AboutAppViewModel(aboutAppInfo: List<AboutAppInfo>) : PluginViewModel() {
    private val _state = MutableStateFlow(value = AboutAppViewState(aboutAppInfo = aboutAppInfo))
    val state: StateFlow<AboutAppViewState> = _state.asStateFlow()
}