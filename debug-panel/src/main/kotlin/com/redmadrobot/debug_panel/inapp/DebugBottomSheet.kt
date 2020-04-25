package com.redmadrobot.debug_panel.inapp

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.debug_panel.R
import kotlinx.android.synthetic.main.bottom_sheet_debug_panel.view.*

open class DebugBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "DebugBottomSheet"

        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                DebugBottomSheet().show(fragmentManager, TAG)
            }
        }
    }

    private var dialogView: View? = null
    private val tabLabelIds = arrayOf(R.string.accounts, R.string.servers, R.string.settings)

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialogView = dialog.layoutInflater.inflate(R.layout.bottom_sheet_debug_panel, null)
        dialog.setOnShowListener {
            setBottomSheetSize()
        }
        dialog.setContentView(requireNotNull(dialogView))
        setViews(requireNotNull(dialogView))
        return dialog
    }

    private fun setBottomSheetSize() {
        val dialogContainer = dialogView?.parent as? FrameLayout
        dialogContainer?.apply {
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            requestLayout()
        }

        with(getBehavior()) {
            peekHeight = resources.displayMetrics.heightPixels / 2
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun setViews(dialogView: View) {
        dialogView.debug_sheet_viewpager.adapter = DebugSheetViewPagerAdapter(requireActivity())
        val tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.setText(tabLabelIds[position])
        }

        TabLayoutMediator(
            dialogView.debug_sheet_tab_layout,
            dialogView.debug_sheet_viewpager,
            tabConfigurationStrategy
        ).attach()
    }

    private fun getBehavior(): BottomSheetBehavior<FrameLayout> {
        return (dialog as BottomSheetDialog).behavior
    }
}
