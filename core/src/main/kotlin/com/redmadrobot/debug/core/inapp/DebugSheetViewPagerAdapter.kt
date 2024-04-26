package com.redmadrobot.debug.core.inapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redmadrobot.debug.core.plugin.Plugin

internal class DebugSheetViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val plugins: List<Plugin>
) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount() = plugins.size

    override fun createFragment(position: Int): Fragment {
        return requireNotNull(plugins[position].getFragment())
    }
}
