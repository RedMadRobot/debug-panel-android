package com.redmadrobot.debug_panel.inapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.debug_panel.R
import kotlinx.android.synthetic.main.bottom_sheet_debug_panel.*

class DebugBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "AccountSelectBottomSheet"

        fun show(fragmentManager: FragmentManager) {
            DebugBottomSheet()
                .show(fragmentManager, TAG)
        }
    }

    private val tabLabelIds = arrayOf(R.string.accounts, R.string.servers, R.string.settings)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_debug_panel, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        debug_sheet_viewpager.adapter = DebugSheetViewPagerAdapter(requireActivity())
        val tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.setText(tabLabelIds[position])
        }

        TabLayoutMediator(
            debug_sheet_tab_layout,
            debug_sheet_viewpager,
            tabConfigurationStrategy
        ).attach()

        val sheetBehavior = (dialog as? BottomSheetDialog)?.behavior
        sheetBehavior?.peekHeight = resources.displayMetrics.heightPixels
    }
}
