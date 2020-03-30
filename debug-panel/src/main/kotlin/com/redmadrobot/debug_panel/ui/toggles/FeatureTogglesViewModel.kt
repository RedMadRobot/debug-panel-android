package com.redmadrobot.debug_panel.ui.toggles

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.data.toggles.FeatureToggleRepository
import com.redmadrobot.debug_panel.data.toggles.model.FeatureToggle
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.toggles.item.FeatureToggleItem
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.rxkotlin.subscribeBy

class FeatureTogglesViewModel(
    private val featureToggleRepository: FeatureToggleRepository
) : BaseViewModel() {

    val featureToggleItems = MutableLiveData<List<Item>>()

    fun loadFeatureToggles() {
        featureToggleRepository.getAllFeatureToggles()
            .observeOnMain()
            .map { items -> items.map { FeatureToggleItem(it, ::updateFeatureToggleValue) } }
            .subscribeBy(onSuccess = { featureToggleItems.value = it })
            .autoDispose()
    }

    fun resetAll() {
        featureToggleRepository.resetAll()
            .subscribeBy(
                onComplete = { loadFeatureToggles() }
            )
            .autoDispose()
    }

    private fun updateFeatureToggleValue(featureToggle: FeatureToggle, newValue: Boolean) {
        featureToggleRepository.updateFeatureToggle(featureToggle.copy(value = newValue))
            .subscribeBy()
            .autoDispose()
    }
}
