package com.redmadrobot.feature_togles_plugin.toggles

import com.redmadrobot.feature_togles_plugin.data.FeatureToggleRepository
import io.reactivex.disposables.CompositeDisposable

class FeatureToggleHolder(private val localFeatureToggleRepository: FeatureToggleRepository) {

    private val disposable = CompositeDisposable()

    private var featureTogglesConfig: FeatureTogglesConfig? = null

    fun initConfig(featureTogglesConfig: FeatureTogglesConfig) {
        this.featureTogglesConfig = featureTogglesConfig
        disposable.add(
            localFeatureToggleRepository.initConfig(featureTogglesConfig)
                .subscribe()
        )
    }
}
