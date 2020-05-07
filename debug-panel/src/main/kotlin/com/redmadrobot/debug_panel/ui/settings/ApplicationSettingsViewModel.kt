package com.redmadrobot.debug_panel.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.data.settings.AppSettingsRepository
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.settings.item.PreferenceBooleanItem
import com.redmadrobot.debug_panel.ui.settings.item.PreferenceValueItem
import com.redmadrobot.debug_panel.ui.view.HeaderItem
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
            items.add(HeaderItem(sharedPreferences.toString()))

            /*Map SharedPreferences to Items*/
            sharedPreferences.all.forEach { (key, value) ->
                val item = if (value is Boolean) {
                    PreferenceBooleanItem(key, value) { settingKey, newValue ->
                        updateSetting(settingKey, newValue)
                    }
                } else {

                    PreferenceValueItem(key, value) { settingKey, newValue ->
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
