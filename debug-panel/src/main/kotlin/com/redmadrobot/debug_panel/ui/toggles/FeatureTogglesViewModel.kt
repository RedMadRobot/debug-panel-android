package com.redmadrobot.debug_panel.ui.toggles

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.data.storage.PreferenceRepository
import com.redmadrobot.debug_panel.data.toggles.FeatureToggleRepository
import com.redmadrobot.debug_panel.data.toggles.model.FeatureToggle
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.toggles.item.FeatureToggleItem
import io.reactivex.rxkotlin.subscribeBy

class FeatureTogglesViewModel(
    private val featureToggleRepository: FeatureToggleRepository,
    private val preferenceRepository: PreferenceRepository
) : BaseViewModel() {

    val screenState = MutableLiveData<FeatureTogglesState>()

    fun loadFeatureToggles() {
        val overrideEnable = preferenceRepository.overrideFeatureToggleEnable
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
