package com.redmadrobot.debug.flipper.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.common.base.PluginViewModel
import com.redmadrobot.debug.flipper.data.FeatureTogglesRepository
import com.redmadrobot.debug.flipper.data.FeatureTogglesSource
import com.redmadrobot.debug.flipper.ui.data.SelectableSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class SourceSelectionViewModel(
    private val repository: FeatureTogglesRepository,
) : PluginViewModel() {

    private val _sources = MutableStateFlow(emptyList<SelectableSource>())
    val sources = _sources.asStateFlow()

    private val _clearingRequest = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val clearingRequest = _clearingRequest.asSharedFlow()

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

                _sources.emit(selectableSources)
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onSelectSource(sourceName: String) {
        repository.setSelectedSource(FeatureTogglesSource(sourceName))
    }
}
