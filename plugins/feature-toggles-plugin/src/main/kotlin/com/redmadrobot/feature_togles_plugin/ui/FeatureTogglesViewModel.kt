package com.redmadrobot.feature_togles_plugin.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_common.extension.safeLaunch
import com.redmadrobot.feature_togles_plugin.data.FeatureToggleRepository
import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle
import com.redmadrobot.feature_togles_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.feature_togles_plugin.ui.item.FeatureToggleItem

class FeatureTogglesViewModel(
    private val featureToggleRepository: FeatureToggleRepository,
    private val pluginSettingsRepository: PluginSettingsRepository
) : PluginViewModel() {

    val screenState = MutableLiveData<FeatureTogglesState>()

    fun loadFeatureToggles() {
        viewModelScope.safeLaunch {
            val overrideEnable = pluginSettingsRepository.overrideFeatureToggleEnable
            val allToggles = featureToggleRepository.getAllFeatureToggles()
            val toggleItems = allToggles.map {
                FeatureToggleItem(it, ::updateFeatureToggleValue, overrideEnable)
            }
            screenState.value = FeatureTogglesState(overrideEnable, toggleItems)
        }
    }

    fun resetAll() {
        viewModelScope.safeLaunch {
            featureToggleRepository.resetAll()
            loadFeatureToggles()
        }
    }

    fun updateOverrideEnable(newOverrideEnable: Boolean) {
        viewModelScope.safeLaunch {
            featureToggleRepository.updateOverrideEnable(newOverrideEnable)
            loadFeatureToggles()
        }
    }

    private fun updateFeatureToggleValue(featureToggle: FeatureToggle, newValue: Boolean) {
        viewModelScope.safeLaunch {
            featureToggleRepository.updateFeatureToggle(featureToggle.copy(value = newValue))
        }
    }
}
