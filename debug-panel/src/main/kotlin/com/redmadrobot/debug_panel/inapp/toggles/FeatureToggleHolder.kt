package com.redmadrobot.debug_panel.inapp.toggles

import com.redmadrobot.debug_panel.data.toggles.FeatureToggleRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class FeatureToggleHolder(
    private val localFeatureToggleRepository: FeatureToggleRepository
) {

    private val disposable = CompositeDisposable()

    private var featureTogglesConfig: FeatureTogglesConfig? = null

    fun initConfig(featureTogglesConfig: FeatureTogglesConfig) {
        this.featureTogglesConfig = featureTogglesConfig
        disposable.add(
            localFeatureToggleRepository.initConfig(featureTogglesConfig)
                .subscribeBy()
        )
    }
}
