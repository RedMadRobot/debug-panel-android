package com.redmadrobot.debug_panel.data.toggles

import com.redmadrobot.debug_panel.data.storage.PreferenceRepository
import com.redmadrobot.debug_panel.data.toggles.model.FeatureToggle
import com.redmadrobot.debug_panel.data.toggles.model.FeatureTogglesDao
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import io.reactivex.Completable
import io.reactivex.Single

class LocalFeatureToggleRepository(
    private val featureTogglesDao: FeatureTogglesDao,
    private val preferenceRepository: PreferenceRepository
) : FeatureToggleRepository {

    private lateinit var featureTogglesConfig: FeatureTogglesConfig

    fun initConfig(featureTogglesConfig: FeatureTogglesConfig): Completable {
        this.featureTogglesConfig = featureTogglesConfig
        val initialFeatureToggles = getInitialFeatureToggles()
        return getAllFeatureToggles()
            .map { actualFeatureToggles ->
                actualFeatureToggles.filterNot { it in initialFeatureToggles }
                    .forEach { toggle ->
                        featureTogglesConfig.changeListener.onFeatureToggleChange(
                            toggle.name,
                            toggle.value
                        )
                    }
            }.ignoreElement()
            .subscribeOnIo()
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
        return if (preferenceRepository.overrideFeatureToggleEnable) {
            featureTogglesDao.getAll()
                .map { localToggles ->
                    initialToggles.map { initialToggle ->
                        val newValue = localToggles.firstOrNull { it.name == initialToggle.name }
                            ?.value
                            ?: initialToggle.value
                        initialToggle.copy(value = newValue)
                    }
                }
        } else {
            Single.just(initialToggles)
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

    private fun getInitialFeatureToggles(): List<FeatureToggle> {
        return featureTogglesConfig.toggleNames
            .map { name ->
                FeatureToggle(name, featureTogglesConfig.featureToggleWrapper.toggleValue(name))
            }
    }
}
