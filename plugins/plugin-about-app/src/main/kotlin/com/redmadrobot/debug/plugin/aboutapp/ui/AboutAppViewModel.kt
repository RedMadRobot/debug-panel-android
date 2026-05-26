package com.redmadrobot.debug.plugin.aboutapp.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppAction
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
import com.redmadrobot.debug.plugin.aboutapp.utils.ClipboardProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

internal class AboutAppViewModel(
    appInfoList: List<AboutAppInfo>,
    actions: List<AboutAppAction>,
    private val context: Context,
    private val clipboardProvider: ClipboardProvider,
    private val pushEvent: (DebugEvent) -> Unit,
) : PluginViewModel() {
    private val _state = MutableStateFlow(
        value = AboutAppViewState(appInfoList = appInfoList, actions = actions),
    )
    val state: StateFlow<AboutAppViewState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AppInfoSelectionEvent>()
    val events = _events.asSharedFlow().distinctUntilChanged()

    fun onInfoItemClicked(message: String, item: AboutAppInfo) {
        clipboardProvider.copyToClipboard(label = item.title, text = item.value)
        viewModelScope.launch {
            _events.emit(AppInfoSelectionEvent(message = message, item = item))
        }
    }

    fun onActionClicked(action: AboutAppAction) {
        when (action) {
            is AboutAppAction.Direct -> action.onClick(context)
            is AboutAppAction.Event -> pushEvent(action.debugEvent)
        }
    }
}

internal data class AppInfoSelectionEvent(
    val message: String,
    val item: AboutAppInfo
) : DebugEvent
