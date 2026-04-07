package com.redmadrobot.debug.plugin.aboutapp.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

internal class AboutAppViewModel(appInfoList: List<AboutAppInfo>) : PluginViewModel() {
    private val _state = MutableStateFlow(value = AboutAppViewState(appInfoList = appInfoList))
    val state: StateFlow<AboutAppViewState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AppInfoSelectionEvent>()
    val events = _events.asSharedFlow().distinctUntilChanged()

    fun onInfoItemClicked(message: String, item: AboutAppInfo) {
        viewModelScope.launch {
            _events.emit(AppInfoSelectionEvent(message = message, item = item))
        }
    }
}

internal data class AppInfoSelectionEvent(
    val message: String,
    val item: AboutAppInfo
) : DebugEvent
