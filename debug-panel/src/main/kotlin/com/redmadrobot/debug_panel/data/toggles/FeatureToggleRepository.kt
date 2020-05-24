package com.redmadrobot.debug_panel.data.toggles

import com.redmadrobot.core.data.storage.entity.FeatureToggle
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig

import io.reactivex.Completable
import io.reactivex.Single

interface FeatureToggleRepository {
    fun initConfig(featureTogglesConfig: FeatureTogglesConfig): Completable
    fun updateFeatureToggle(featureToggle: FeatureToggle): Completable
    fun getAllFeatureToggles(): Single<List<FeatureToggle>>
    fun resetAll(): Completable
    fun updateOverrideEnable(newOverrideEnable: Boolean): Completable
}
