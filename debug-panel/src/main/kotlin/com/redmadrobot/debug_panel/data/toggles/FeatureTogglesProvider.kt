package com.redmadrobot.debug_panel.data.toggles

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.toggles.model.FeatureToggle
import io.reactivex.Single

class FeatureTogglesProvider(
    private val dataLoadingStrategy: DebugDataLoadingStrategy<FeatureToggle>
) {

    fun loadServers(): Single<List<FeatureToggle>> {
        return dataLoadingStrategy.loadData()
    }
}
