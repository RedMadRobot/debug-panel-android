package com.redmadrobot.feature_togles_plugin.data

import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle
import com.redmadrobot.feature_togles_plugin.toggles.FeatureTogglesConfig

import io.reactivex.Completable
import io.reactivex.Single

interface FeatureToggleRepository {
    fun initConfig(featureTogglesConfig: FeatureTogglesConfig): Completable
    fun updateFeatureToggle(featureToggle: FeatureToggle): Completable
    fun getAllFeatureToggles(): Single<List<FeatureToggle>>
    fun resetAll(): Completable
    fun updateOverrideEnable(newOverrideEnable: Boolean): Completable
}
