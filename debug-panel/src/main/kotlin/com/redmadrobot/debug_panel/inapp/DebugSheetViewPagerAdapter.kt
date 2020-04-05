package com.redmadrobot.debug_panel.inapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.redmadrobot.debug_panel.ui.StubFragment
import com.redmadrobot.debug_panel.ui.accounts.choose.AccountSelectionFragment
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesFragment

internal class DebugSheetViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val PAGER_SIZE = 3
    }

    override fun getItemCount() = PAGER_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AccountSelectionFragment()
            1 -> StubFragment()
            2 -> FeatureTogglesFragment()
            else -> throw IllegalArgumentException("Nonexistent page number")
        }
    }
}
