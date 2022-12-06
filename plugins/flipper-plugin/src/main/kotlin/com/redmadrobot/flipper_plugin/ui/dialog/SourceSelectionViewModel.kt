package com.redmadrobot.flipper_plugin.ui.dialog

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.common.base.PluginViewModel
import com.redmadrobot.flipper_plugin.R
import com.redmadrobot.flipper_plugin.data.FeatureTogglesRepository
import com.redmadrobot.flipper_plugin.data.FeatureTogglesSource
import com.redmadrobot.flipper_plugin.ui.dialog.data.SelectableSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class SourceSelectionViewModel(
    private val context: Context,
    private val repository: FeatureTogglesRepository,
) : PluginViewModel() {

    private val _sources = MutableStateFlow(emptyList<SelectableSource>())
    val sources = _sources.asStateFlow()

    private val _clearingRequest = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val clearingRequest = _clearingRequest.asSharedFlow()

    private val clearingSource = SelectableSource(
        name = context.getString(R.string.flipper_plugin_clear_changes),
        selected = false,
    )

    init {
        combine(
            repository.getSources(),
            repository.getSelectedSource(),
        ) { sources, selectedSource ->
            sources.keys to selectedSource
        }
            .onEach { (sources, selectedSource) ->
                val selectableSources = sources.map { source ->
                    SelectableSource(
                        name = source.name,
                        selected = source == selectedSource,
                    )
                }

                _sources.emit(listOf(clearingSource) + selectableSources)
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onSelectSource(sourceName: String) {
        if (clearingSource.name == sourceName) {
            _clearingRequest.tryEmit(Unit)
            return
        }
        repository.setSelectedSource(
            FeatureTogglesSource(sourceName)
        )
    }

    fun onClearTogglesConfirm() {
        viewModelScope.launch {
            repository.resetAllToDefault()
        }
    }
}
