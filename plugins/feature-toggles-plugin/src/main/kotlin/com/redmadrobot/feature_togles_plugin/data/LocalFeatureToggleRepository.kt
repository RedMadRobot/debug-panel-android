package com.redmadrobot.feature_togles_plugin.data

import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle
import com.redmadrobot.feature_togles_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.feature_togles_plugin.data.storage.FeatureTogglesDao
import com.redmadrobot.feature_togles_plugin.toggles.FeatureTogglesConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalFeatureToggleRepository(
    private val featureTogglesDao: FeatureTogglesDao,
    private val pluginSettingsRepository: PluginSettingsRepository
) : FeatureToggleRepository {

    private lateinit var featureTogglesConfig: FeatureTogglesConfig

    override suspend fun initConfig(featureTogglesConfig: FeatureTogglesConfig) {
        this@LocalFeatureToggleRepository.featureTogglesConfig = featureTogglesConfig
        if (pluginSettingsRepository.overrideFeatureToggleEnable) {
            updateInitialToggles(true)
        }
    }

    override suspend fun updateFeatureToggle(featureToggle: FeatureToggle) {
        withContext(Dispatchers.IO) {
            featureTogglesDao.insert(featureToggle)
            featureTogglesConfig.changeListener.onFeatureToggleChange(
                featureToggle.name,
                featureToggle.value
            )
        }
    }

    override suspend fun getAllFeatureToggles(): List<FeatureToggle> {
        return withContext(Dispatchers.IO) {
            val initialToggles = getInitialFeatureToggles()
            val localToggles = featureTogglesDao.getAll()

            initialToggles.map { initialToggle ->
                val newValue = localToggles.firstOrNull { it.name == initialToggle.name }
                    ?.value ?: initialToggle.value
                initialToggle.copy(value = newValue)
            }
        }
    }

    override suspend fun resetAll() {
        withContext(Dispatchers.IO) {
            val initialFeatureToggles = getInitialFeatureToggles()
            val localToggles = featureTogglesDao.getAll()
            initialFeatureToggles.filterNot { it in localToggles }
                .forEach { (name, value) ->
                    featureTogglesConfig.changeListener.onFeatureToggleChange(name, value)
                }
            featureTogglesDao.insertAll(initialFeatureToggles)
        }
    }


    override suspend fun updateOverrideEnable(newOverrideEnable: Boolean) {
        withContext(Dispatchers.IO) {
            if (pluginSettingsRepository.overrideFeatureToggleEnable != newOverrideEnable) {
                pluginSettingsRepository.overrideFeatureToggleEnable = newOverrideEnable
                updateInitialToggles(newOverrideEnable)
            }
        }
    }


    private suspend fun updateInitialToggles(overrideEnable: Boolean) {
        val initialToggles = getInitialFeatureToggles()
        val localToggles = featureTogglesDao.getAll()

        if (overrideEnable) {
            localToggles.filterNot { it in initialToggles }
        } else {
            initialToggles.filterNot { it in localToggles }
        }
            .forEach { (name, value) ->
                featureTogglesConfig.changeListener.onFeatureToggleChange(name, value)
            }
    }

    private fun getInitialFeatureToggles(): List<FeatureToggle> {
        return featureTogglesConfig.toggleNames
            .map { name ->
                FeatureToggle(
                    name,
                    featureTogglesConfig.featureToggleWrapper.toggleValue(name)
                )
            }
    }
}
