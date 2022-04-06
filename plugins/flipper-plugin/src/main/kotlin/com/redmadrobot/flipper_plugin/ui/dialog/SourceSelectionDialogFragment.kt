package com.redmadrobot.flipper_plugin.ui.dialog

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.flipper_plugin.R
import com.redmadrobot.flipper_plugin.databinding.DialogFlipperSourceBinding
import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
import com.redmadrobot.flipper_plugin.plugin.FlipperPluginContainer
import com.redmadrobot.flipper_plugin.ui.dialog.recycler.SourceSelectionAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class SourceSelectionDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: SourceSelectionViewModel by lazy {
        obtainShareViewModel {
            getPlugin<FlipperPlugin>()
                .getContainer<FlipperPluginContainer>()
                .createFlipperSourceSelectionViewModel()
        }
    }

    private lateinit var sourcesAdapter: SourceSelectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogFlipperSourceBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DialogFlipperSourceBinding.bind(view)

        sourcesAdapter = SourceSelectionAdapter(
            onSourceClick = { sourceName ->
                viewModel.onSelectSource(sourceName)
            },
        )

        binding.root.adapter = sourcesAdapter

        viewModel.sources
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach(sourcesAdapter::submitList)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.clearingRequest
            .onEach {
                showResetConfirmationDialog()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.flipper_plugin_dialog_title_feature_toggles_reset)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                viewModel.onClearTogglesConfirm()

                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
