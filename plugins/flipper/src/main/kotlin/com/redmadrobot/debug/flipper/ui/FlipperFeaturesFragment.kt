package com.redmadrobot.debug.flipper.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.debug.common.base.PluginFragment
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.flipper.R
import com.redmadrobot.debug.flipper.databinding.FragmentFlipperFeaturesBinding
import com.redmadrobot.debug.flipper.plugin.FlipperPlugin
import com.redmadrobot.debug.flipper.plugin.FlipperPluginContainer
import com.redmadrobot.debug.flipper.ui.dialog.SourceSelectionDialogFragment
import com.redmadrobot.debug.flipper.ui.recycler.FlipperFeaturesAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class FlipperFeaturesFragment : PluginFragment(R.layout.fragment_flipper_features) {

    private var _binding: FragmentFlipperFeaturesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FlipperFeaturesViewModel by lazy {
        obtainShareViewModel {
            getPlugin<FlipperPlugin>()
                .getContainer<FlipperPluginContainer>()
                .createFlipperFeaturesViewModel()
        }
    }

    private lateinit var featuresAdapter: FlipperFeaturesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFlipperFeaturesBinding.bind(view)

        this.featuresAdapter = FlipperFeaturesAdapter(
            onGroupClick = viewModel::onGroupClick,
            onFeatureValueChanged = viewModel::onFeatureValueChanged,
            onGroupToggleStateChanged = viewModel::onGroupToggleStateChanged,
        )

        binding.featureList.adapter = this.featuresAdapter
        binding.query.addTextChangedListener { text ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
        }
        binding.resetToDefault.setOnClickListener {
            SourceSelectionDialogFragment()
                .show(childFragmentManager, SourceSelectionDialogFragment::class.simpleName)
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                featuresAdapter.submitList(state.items)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

}
