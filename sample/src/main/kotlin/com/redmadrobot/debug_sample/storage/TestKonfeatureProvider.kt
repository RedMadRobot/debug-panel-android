package com.redmadrobot.debug_sample.storage

import com.redmadrobot.debug.plugin.konfeature.KonfeatureDebugPanelInterceptor
import com.redmadrobot.konfeature.FeatureConfig
import com.redmadrobot.konfeature.Konfeature
import com.redmadrobot.konfeature.Logger
import com.redmadrobot.konfeature.builder.konfeature
import com.redmadrobot.konfeature.source.FeatureSource
import com.redmadrobot.konfeature.source.SourceSelectionStrategy
import timber.log.Timber

internal object TestKonfeatureProvider {

    fun create(debugPanelInterceptor: KonfeatureDebugPanelInterceptor): Konfeature {
        return konfeature {
            register(FeatureConfig1())
            register(FeatureConfig2())
            addInterceptor(debugPanelInterceptor)
            addSource(object : FeatureSource {
                override val name: String = "SampleFeatureSource"

                override fun get(key: String): Any? {
                    return key == "boolean_feature_2"
                }
            })
            setLogger(object : Logger {
                override fun log(severity: Logger.Severity, message: String) {
                    when (severity) {
                        Logger.Severity.WARNING -> Timber.tag("Konfeature").w(message)
                        Logger.Severity.INFO -> Timber.tag("Konfeature").i(message)
                    }
                }
            })
        }
    }

    class FeatureConfig1 : FeatureConfig(
        name = "FeatureConfig1",
        description = "feature config number one"
    ) {
        val booleanFeature1 by toggle(
            key = "boolean_feature_1",
            description = "boolean feature one",
            defaultValue = false,
        )

        val booleanFeature2 by toggle(
            key = "boolean_feature_2",
            description = "boolean feature two",
            defaultValue = false,
            sourceSelectionStrategy = SourceSelectionStrategy.Any
        )

        val doubleFeature1: Double by value(
            key = "double_feature_1",
            description = "double feature one",
            defaultValue = 999.99,
        )

        val stringFeature1: String by value(
            key = "string_feature_1",
            description = "string feature one",
            defaultValue = "String feature 1",
        )
    }

    class FeatureConfig2 : FeatureConfig(
        name = "FeatureConfig2",
        description = "feature config number two"
    ) {
        val booleanFeature3 by toggle(
            key = "boolean_feature_3",
            description = "boolean feature three",
            defaultValue = false,
        )

        val booleanFeature4 by toggle(
            key = "boolean_feature_4",
            description = "boolean feature foure",
            defaultValue = false,
        )

        val longFeature1: Long by value(
            key = "long_feature_1",
            description = "long feature one",
            defaultValue = 100,
        )
    }

}
