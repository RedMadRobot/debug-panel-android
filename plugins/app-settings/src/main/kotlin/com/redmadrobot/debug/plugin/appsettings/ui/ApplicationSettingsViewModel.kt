package com.redmadrobot.debug.plugin.appsettings.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug.plugin.appsettings.data.AppSettingsRepository
import com.redmadrobot.debug.plugin.appsettings.ui.item.AppSettingItems
import com.redmadrobot.debug.common.base.PluginViewModel

internal class ApplicationSettingsViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : PluginViewModel() {

    val settingsLiveData = MutableLiveData<List<AppSettingItems>>()

    fun loadSettings() {
        val settings = appSettingsRepository.getSettings()
        val settingItems = mapToItems(settings)
        settingsLiveData.value = settingItems
    }

    @Suppress("NewApi")
    private fun mapToItems(settings: List<SharedPreferences>): List<AppSettingItems> {
        val items = mutableListOf<AppSettingItems>()
        settings.forEach { sharedPreferences ->
            /*Settings header*/
            items.add(
                AppSettingItems.Header(sharedPreferences.toString())
            )

            /*Map SharedPreferences to Items*/
            sharedPreferences.all.forEach { (key, value) ->
                val item = if (value is Boolean) {
                    AppSettingItems.BooleanValueItem(key, value) { settingKey, newValue ->
                        updateSetting(settingKey, newValue)
                    }
                } else {
                    AppSettingItems.ValueItem(key, value) { settingKey, newValue ->
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
    }
}
