package com.redmadrobot.debug_panel.data.toggles

import com.redmadrobot.debug_panel.data.storage.PanelSettingsRepository
import com.redmadrobot.debug_panel.data.toggles.model.FeatureToggle
import com.redmadrobot.debug_panel.data.toggles.model.FeatureTogglesDao
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import io.reactivex.Completable
import io.reactivex.Single

class LocalFeatureToggleRepository(
    private val featureTogglesDao: FeatureTogglesDao,
    private val panelSettingsRepository: PanelSettingsRepository
) : FeatureToggleRepository {

    private lateinit var featureTogglesConfig: FeatureTogglesConfig

    override fun initConfig(featureTogglesConfig: FeatureTogglesConfig): Completable {
        this.featureTogglesConfig = featureTogglesConfig
        return if (panelSettingsRepository.overrideFeatureToggleEnable) {
            updateInitialToggles(true)
        } else {
            Completable.complete()
        }
    }

    override fun updateFeatureToggle(featureToggle: FeatureToggle): Completable {
        return featureTogglesDao.insert(featureToggle)
            .doOnComplete {
                featureTogglesConfig.changeListener.onFeatureToggleChange(
                    featureToggle.name,
                    featureToggle.value
                )
            }
            .subscribeOnIo()
    }

    override fun getAllFeatureToggles(): Single<List<FeatureToggle>> {
        val initialToggles = getInitialFeatureToggles()
        return featureTogglesDao.getAll()
            .map { localToggles ->
                initialToggles.map { initialToggle ->
                    val newValue = localToggles.firstOrNull { it.name == initialToggle.name }
                        ?.value
                        ?: initialToggle.value
                    initialToggle.copy(value = newValue)
                }
            }
            .subscribeOnIo()
    }

    override fun resetAll(): Completable {
        val initialFeatureToggles = getInitialFeatureToggles()
        return featureTogglesDao.getAll()
            .map { localToggles ->
                initialFeatureToggles.filterNot { it in localToggles }
                    .forEach { (name, value) ->
                        featureTogglesConfig.changeListener.onFeatureToggleChange(name, value)
                    }
            }
            .ignoreElement()
            .andThen(featureTogglesDao.insertAll(initialFeatureToggles))
            .subscribeOnIo()
    }


    override fun updateOverrideEnable(newOverrideEnable: Boolean): Completable {
        return if (panelSettingsRepository.overrideFeatureToggleEnable != newOverrideEnable) {
            panelSettingsRepository.overrideFeatureToggleEnable = newOverrideEnable
            updateInitialToggles(newOverrideEnable)
        } else {
            Completable.complete()
        }
    }


    private fun updateInitialToggles(overrideEnable: Boolean): Completable {
        val initialToggles = getInitialFeatureToggles()
        return featureTogglesDao.getAll()
            .map { localToggles ->
                if (overrideEnable) {
                    localToggles.filterNot { it in initialToggles }
                } else {
                    initialToggles.filterNot { it in localToggles }
                }
                    .forEach { (name, value) ->
                        featureTogglesConfig.changeListener.onFeatureToggleChange(name, value)
                    }
            }
            .ignoreElement()
            .subscribeOnIo()
    }

    private fun getInitialFeatureToggles(): List<FeatureToggle> {
        return featureTogglesConfig.toggleNames
            .map { name ->
                FeatureToggle(name, featureTogglesConfig.featureToggleWrapper.toggleValue(name))
            }
    }
}
