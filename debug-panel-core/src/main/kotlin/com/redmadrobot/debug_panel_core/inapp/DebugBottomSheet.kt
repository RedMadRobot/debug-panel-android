package com.redmadrobot.debug_panel_core.inapp

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.debug_panel_core.extension.getAllPlugins
import com.redmadrobot.panel_core.R
import kotlinx.android.synthetic.main.bottom_sheet_debug_panel.view.*


internal class DebugBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "DebugBottomSheet"

        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                DebugBottomSheet().show(fragmentManager, TAG)
            }
        }
    }

    private var dialogView: View? = null

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /*set DebugPanelTheme*/
        val dialog = super.onCreateDialog(savedInstanceState)
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.DebugPanelTheme)
        val localInflater = dialog.layoutInflater.cloneInContext(contextThemeWrapper)

        dialogView = localInflater.inflate(R.layout.bottom_sheet_debug_panel, null)
        dialog.setOnShowListener {
            setBottomSheetSize()
        }
        dialog.setContentView(requireNotNull(dialogView))
        setViews(requireNotNull(dialogView))
        return dialog
    }

    private fun setViews(dialogView: View) {
        val plugins = getAllPlugins()
            /*Only Plugins with Fragment*/
            .filter { it.getFragment() != null }

        dialogView.debug_sheet_viewpager.adapter = DebugSheetViewPagerAdapter(
            requireActivity(),
            plugins
        )

        val tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = plugins[position].getName()
        }

        TabLayoutMediator(
            dialogView.debug_sheet_tab_layout,
            dialogView.debug_sheet_viewpager,
            tabConfigurationStrategy
        ).attach()
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

    private fun getBehavior(): BottomSheetBehavior<FrameLayout> {
        return (dialog as BottomSheetDialog).behavior
    }
}
