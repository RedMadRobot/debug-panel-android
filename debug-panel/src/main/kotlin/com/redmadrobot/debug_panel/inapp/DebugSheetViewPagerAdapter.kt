package com.redmadrobot.debug_panel.inapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redmadrobot.account_plugin.ui.select.AccountSelectionFragment
import com.redmadrobot.debug_panel.ui.servers.choose.ServerSelectionFragment
import com.redmadrobot.debug_panel.ui.settings.ApplicationSettingsFragment
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesFragment

internal class DebugSheetViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val PAGER_SIZE = 4
    }

    override fun getItemCount() = PAGER_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AccountSelectionFragment()
            1 -> ServerSelectionFragment()
            2 -> FeatureTogglesFragment()
            3 -> ApplicationSettingsFragment()
            else -> throw IllegalArgumentException("Nonexistent page number")
        }
    }
}
