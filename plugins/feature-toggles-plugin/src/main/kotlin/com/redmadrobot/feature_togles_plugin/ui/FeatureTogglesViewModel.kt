package com.redmadrobot.feature_togles_plugin.ui

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_core.extension.observeOnMain
import com.redmadrobot.feature_togles_plugin.data.FeatureToggleRepository
import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle
import com.redmadrobot.feature_togles_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.feature_togles_plugin.ui.item.FeatureToggleItem
import timber.log.Timber

class FeatureTogglesViewModel(
    private val featureToggleRepository: FeatureToggleRepository,
    private val pluginSettingsRepository: PluginSettingsRepository
) : PluginViewModel() {

    val screenState = MutableLiveData<FeatureTogglesState>()

    fun loadFeatureToggles() {
        val overrideEnable = pluginSettingsRepository.overrideFeatureToggleEnable
        featureToggleRepository.getAllFeatureToggles()
            .observeOnMain()
            .map { items ->
                items.map { FeatureToggleItem(it, ::updateFeatureToggleValue, overrideEnable) }
            }
            .subscribe(
                { screenState.value = FeatureTogglesState(overrideEnable, it) },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    fun resetAll() {
        featureToggleRepository.resetAll()
            .subscribe(
                { loadFeatureToggles() },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    fun updateOverrideEnable(newOverrideEnable: Boolean) {
        featureToggleRepository.updateOverrideEnable(newOverrideEnable)
            .subscribe(
                { loadFeatureToggles() },
                { Timber.e(it) })
            .autoDispose()
    }

    private fun updateFeatureToggleValue(featureToggle: FeatureToggle, newValue: Boolean) {
        featureToggleRepository.updateFeatureToggle(featureToggle.copy(value = newValue))
            .subscribe(
                {/*do nothing*/},
                { Timber.e(it) }
            )
            .autoDispose()
    }
}
