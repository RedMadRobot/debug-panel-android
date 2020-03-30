package com.redmadrobot.debug_panel.data.toggles

import com.redmadrobot.debug_panel.data.toggles.model.FeatureToggle
import io.reactivex.Completable
import io.reactivex.Single

interface FeatureToggleRepository {
    fun updateFeatureToggle(featureToggle: FeatureToggle): Completable
    fun getAllFeatureToggles(): Single<List<FeatureToggle>>
    fun resetAll(): Completable
}
