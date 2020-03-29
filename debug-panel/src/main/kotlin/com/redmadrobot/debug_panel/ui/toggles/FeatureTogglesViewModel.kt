package com.redmadrobot.debug_panel.ui.toggles

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggle
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleHolder
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.toggles.item.FeatureToggleItem
import com.xwray.groupie.kotlinandroidextensions.Item

class FeatureTogglesViewModel(
    private val featureToggleHolder: FeatureToggleHolder
) : BaseViewModel() {

    val featureToggleItems = MutableLiveData<List<Item>>()

    fun loadFeatureToggles() {
        featureToggleItems.value = featureToggleHolder.getFeatureToggles()
            .map { FeatureToggleItem(it, ::updateFeatureToggleValue) }
    }

    fun resetAll() {
        featureToggleHolder.resetAll()
        loadFeatureToggles()
    }

    private fun updateFeatureToggleValue(featureToggle: FeatureToggle, newValue: Boolean) {
        featureToggleHolder.updateFeatureToggle(featureToggle.name, newValue)
    }
}
