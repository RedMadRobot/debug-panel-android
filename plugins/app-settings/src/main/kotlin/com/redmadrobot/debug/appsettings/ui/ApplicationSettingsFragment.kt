package com.redmadrobot.debug.appsettings.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.app_settings_plugin.R
import com.redmadrobot.debug.appsettings.plugin.AppSettingsPlugin
import com.redmadrobot.debug.appsettings.plugin.AppSettingsPluginContainer
import com.redmadrobot.debug.appsettings.ui.item.AppSettingItems
import com.redmadrobot.debug.common.base.PluginFragment
import com.redmadrobot.debug.common.extension.observe
import com.redmadrobot.debug.common.extension.obtainViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.itemsadapter.itemsAdapter
import kotlinx.android.synthetic.main.fragment_app_settings.*

internal class ApplicationSettingsFragment : PluginFragment(R.layout.fragment_app_settings) {

    private val settingsViewModel by lazy {
        obtainViewModel {
            getPlugin<AppSettingsPlugin>()
                .getContainer<AppSettingsPluginContainer>()
                .createApplicationSettingsViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(settingsViewModel.settingsLiveData, ::setSettingList)
        setViews()
        settingsViewModel.loadSettings()
    }

    private fun setViews() {
        app_settings.layoutManager = LinearLayoutManager(context)
    }

    private fun setSettingList(settings: List<AppSettingItems>) {
        app_settings.adapter = itemsAdapter(settings) { item ->
            item.getItem(this)
        }
    }
}
