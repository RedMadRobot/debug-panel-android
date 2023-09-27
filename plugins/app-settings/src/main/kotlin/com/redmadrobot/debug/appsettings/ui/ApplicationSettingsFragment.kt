package com.redmadrobot.debug.appsettings.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug.app_settings.R
import com.redmadrobot.debug.app_settings.databinding.FragmentAppSettingsBinding
import com.redmadrobot.debug.appsettings.plugin.AppSettingsPlugin
import com.redmadrobot.debug.appsettings.plugin.AppSettingsPluginContainer
import com.redmadrobot.debug.appsettings.ui.item.AppSettingItems
import com.redmadrobot.debug.common.base.PluginFragment
import com.redmadrobot.debug.common.extension.observe
import com.redmadrobot.debug.common.extension.obtainViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.itemsadapter.itemsAdapter

internal class ApplicationSettingsFragment : PluginFragment(R.layout.fragment_app_settings) {

    private var binding: FragmentAppSettingsBinding? = null

    private val settingsViewModel by lazy {
        obtainViewModel {
            getPlugin<AppSettingsPlugin>()
                .getContainer<AppSettingsPluginContainer>()
                .createApplicationSettingsViewModel()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(settingsViewModel.settingsLiveData, ::setSettingList)
        settingsViewModel.loadSettings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAppSettingsBinding.bind(view).also {
            it.setViews()
        }
    }

    private fun FragmentAppSettingsBinding.setViews() {
        appSettings.layoutManager = LinearLayoutManager(context)
    }

    private fun setSettingList(settings: List<AppSettingItems>) {
        val binding = binding ?: return
        binding.appSettings.adapter = itemsAdapter(settings) { item ->
            item.getItem(this)
        }
    }
}
