package com.redmadrobot.debug_panel.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.core.extension.getPlugin
import com.redmadrobot.core.extension.observe
import com.redmadrobot.core.extension.obtainViewModel
import com.redmadrobot.core.ui.base.BaseFragment
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.internal.plugin.app_settings.AppSettingsPlugin
import com.redmadrobot.debug_panel.internal.plugin.app_settings.AppSettingsPluginContainer
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_app_settings.*

class ApplicationSettingsFragment : BaseFragment(R.layout.fragment_app_settings) {

    private val settingsViewModel by lazy {
        obtainViewModel {
            getPlugin<AppSettingsPlugin>()
                .getContainer<AppSettingsPluginContainer>()
                .createApplicationSettingsViewModel()
        }
    }

    private val settingsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(settingsViewModel.settings, ::setSettingList)
        settingsViewModel.loadSettings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        app_settings.adapter = settingsAdapter
        app_settings.layoutManager = LinearLayoutManager(context)
    }

    private fun setSettingList(settings: List<Item>) {
        settingsAdapter.update(settings)
    }
}
