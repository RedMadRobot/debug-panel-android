package com.redmadrobot.app_settings_plugin.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.app_settings_plugin.data.AppSettingsRepository
import com.redmadrobot.app_settings_plugin.ui.item.AppSettingBooleanItem
import com.redmadrobot.app_settings_plugin.ui.item.AppSettingValueItem
import com.redmadrobot.app_settings_plugin.ui.item.HeaderItem
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.xwray.groupie.kotlinandroidextensions.Item

internal class ApplicationSettingsViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : PluginViewModel() {

    val settingsLiveData = MutableLiveData<List<Item>>()

    fun loadSettings() {
        val settings = appSettingsRepository.getSettings()
        val settingItems = mapToItems(settings)
        settingsLiveData.value = settingItems
    }

    private fun mapToItems(settings: List<SharedPreferences>): List<Item> {
        val items = mutableListOf<Item>()
        settings.forEach { sharedPreferences ->
            /*Settings header*/
            items.add(
                HeaderItem(sharedPreferences.toString())
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
    }
}
