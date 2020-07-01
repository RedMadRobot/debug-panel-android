package com.redmadrobot.debug_panel.inapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redmadrobot.core.plugin.Plugin

internal class DebugSheetViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val plugins: List<Plugin>
) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount() = plugins.size

    override fun createFragment(position: Int): Fragment {
        return requireNotNull(plugins[position].getFragment())
    }
}
