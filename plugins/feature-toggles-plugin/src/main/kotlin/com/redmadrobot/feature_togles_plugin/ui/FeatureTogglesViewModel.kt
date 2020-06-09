package com.redmadrobot.feature_togles_plugin.ui

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.core.data.storage.PanelSettingsRepository
import com.redmadrobot.core.data.storage.entity.FeatureToggle
import com.redmadrobot.core.extension.observeOnMain
import com.redmadrobot.core.ui.base.BaseViewModel
import com.redmadrobot.feature_togles_plugin.data.FeatureToggleRepository
import com.redmadrobot.feature_togles_plugin.ui.item.FeatureToggleItem
import io.reactivex.rxkotlin.subscribeBy

class FeatureTogglesViewModel(
    private val featureToggleRepository: FeatureToggleRepository,
    private val panelSettingsRepository: PanelSettingsRepository
) : BaseViewModel() {

    val screenState = MutableLiveData<FeatureTogglesState>()

    fun loadFeatureToggles() {
        val overrideEnable = panelSettingsRepository.overrideFeatureToggleEnable
        featureToggleRepository.getAllFeatureToggles()
            .observeOnMain()
            .map { items ->
                items.map { FeatureToggleItem(it, ::updateFeatureToggleValue, overrideEnable) }
            }
            .subscribeBy(
                onSuccess = { screenState.value = FeatureTogglesState(overrideEnable, it) }
            )
            .autoDispose()
    }

    fun resetAll() {
        featureToggleRepository.resetAll()
            .subscribeBy(onComplete = { loadFeatureToggles() })
            .autoDispose()
    }

    fun updateOverrideEnable(newOverrideEnable: Boolean) {
        featureToggleRepository.updateOverrideEnable(newOverrideEnable)
            .subscribeBy(onComplete = { loadFeatureToggles() })
            .autoDispose()
    }

    private fun updateFeatureToggleValue(featureToggle: FeatureToggle, newValue: Boolean) {
        featureToggleRepository.updateFeatureToggle(featureToggle.copy(value = newValue))
            .subscribeBy()
            .autoDispose()
    }
}
