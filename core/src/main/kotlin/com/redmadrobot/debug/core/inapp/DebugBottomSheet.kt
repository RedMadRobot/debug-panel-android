package com.redmadrobot.debug.core.inapp

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.debug.core.extension.getAllPlugins
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

    private var binding: BottomSheetDebugPanelBinding? = null

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /*set DebugPanelTheme*/
        val dialog = super.onCreateDialog(savedInstanceState)
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.DebugPanelTheme)
        val localInflater = dialog.layoutInflater.cloneInContext(contextThemeWrapper)
        binding = BottomSheetDebugPanelBinding.inflate(localInflater)
        dialog.setOnShowListener {
            setBottomSheetSize()
        }
        dialog.setContentView(requireNotNull(binding?.root))
        setViews()
        return dialog
    }

    private fun setViews() = binding?.let { binding ->

        val plugins = getAllPlugins()
            /*Only Plugins with Fragment*/
            .filter { it.getFragment() != null }

        binding.debugSheetViewpager.adapter = DebugSheetViewPagerAdapter(
            requireActivity(),
            plugins
        )

        val tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = plugins[position].getName()
        }

        TabLayoutMediator(
            binding.debugSheetTabLayout,
            binding.debugSheetViewpager,
            tabConfigurationStrategy
        ).attach()
    }

    private fun setBottomSheetSize() {
        val dialogContainer = binding?.root?.parent as? FrameLayout
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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
