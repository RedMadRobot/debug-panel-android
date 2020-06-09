package com.redmadrobot.app_settings_plugin.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.app_settings_plugin.data.AppSettingsRepository
import com.redmadrobot.app_settings_plugin.ui.item.AppSettingBooleanItem
import com.redmadrobot.app_settings_plugin.ui.item.AppSettingValueItem
import com.redmadrobot.app_settings_plugin.ui.item.HeaderItem
import com.redmadrobot.core.extension.observeOnMain
import com.redmadrobot.core.ui.base.BaseViewModel
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class ApplicationSettingsViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : BaseViewModel() {

    val settings = MutableLiveData<List<Item>>()

    fun loadSettings() {
        appSettingsRepository.getSettings()
            .map { mapToItems(it) }
            .observeOnMain()
            .subscribe(
                { settingItems ->
                    settings.value = settingItems
                },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    private fun mapToItems(settings: List<SharedPreferences>): List<Item> {
        val items = mutableListOf<Item>()
        settings.forEach { sharedPreferences ->
            /*Settings header*/
            items.add(
                HeaderItem(
                    sharedPreferences.toString()
                )
            )

            /*Map SharedPreferences to Items*/
            sharedPreferences.all.forEach { (key, value) ->
                val item = if (value is Boolean) {
                    AppSettingBooleanItem(key, value) { settingKey, newValue ->
                        updateSetting(settingKey, newValue)
                    }
                } else {
                    AppSettingValueItem(key, value) { settingKey, newValue ->
                        updateSetting(settingKey, newValue)
                    }
                }
                items.add(item)
            }
        }
        return items
    }

    private fun updateSetting(settingKey: String, newValue: Any) {
        appSettingsRepository.updateSetting(settingKey, newValue)
            .observeOnMain()
            .subscribeBy(onError = { Timber.e(it) })
            .autoDispose()
    }
}
