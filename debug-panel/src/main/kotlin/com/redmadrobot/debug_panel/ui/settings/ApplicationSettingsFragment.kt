package com.redmadrobot.debug_panel.ui.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.ui.base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_app_settings.*

class ApplicationSettingsFragment : BaseFragment(R.layout.fragment_app_settings) {

    companion object {
        fun getInstance() = ApplicationSettingsFragment()
    }

    private val settingsAdapter = GroupAdapter<GroupieViewHolder>()


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
