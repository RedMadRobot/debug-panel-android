package com.redmadrobot.debug_panel.ui.toggles

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggle
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleHolder
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.toggles.item.FeatureToggleItem
import com.xwray.groupie.kotlinandroidextensions.Item

class FeatureTogglesViewModel : BaseViewModel() {

    val featureToggleItems = MutableLiveData<List<Item>>()

    fun loadFeatureToggles() {
        featureToggleItems.value = FeatureToggleHolder.mockToggleWrapper.featureToggles
            .map { FeatureToggleItem(it, ::updateFeatureToggleValue) }
    }

    private fun updateFeatureToggleValue(featureToggle: FeatureToggle, newValue: Boolean) {
        val newFeatureToggles = FeatureToggleHolder.mockToggleWrapper.featureToggles.map {
            it.takeIf { featureToggle.name == it.name }
                ?.copy(value = newValue)
                ?: it
        }
        FeatureToggleHolder.mockToggleWrapper.featureToggles = newFeatureToggles
        loadFeatureToggles()
    }
}
