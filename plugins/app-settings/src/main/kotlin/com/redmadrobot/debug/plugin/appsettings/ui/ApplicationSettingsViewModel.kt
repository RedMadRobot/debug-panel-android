package com.redmadrobot.debug.plugin.appsettings.ui

import android.content.SharedPreferences
import com.redmadrobot.debug.plugin.appsettings.data.AppSettingsRepository
import com.redmadrobot.debug.common.base.PluginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class ApplicationSettingsViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : PluginViewModel() {

    val state = MutableStateFlow<List<SettingItemUiModel>>(emptyList())

    fun loadSettings() {
        val settings = appSettingsRepository.getSettings()
        val newSettingItems = mapToItemsNew(settings)
        state.update { newSettingItems }
    }

    fun updateSetting(settingKey: String, newValue: Any) {
        appSettingsRepository.updateSetting(settingKey, newValue)
        loadSettings()
    }

    private fun mapToItemsNew(settings: List<SharedPreferences>): MutableList<SettingItemUiModel> {
        val items = mutableListOf<SettingItemUiModel>()
        settings.forEach { sharedPreferences ->
            /*Settings header*/
            items.add(
                SettingItemUiModel.Header(sharedPreferences.toString())
            )

            /*Map SharedPreferences to Items*/
            sharedPreferences.all.forEach { (key, value) ->
                val item = if (value is Boolean) {
                    SettingItemUiModel.BooleanItem(key, value)
                } else {
                    SettingItemUiModel.ValueItem(key, value)
                }
                items.add(item)
            }
        }
        return items
    }
}
