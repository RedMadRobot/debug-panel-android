package com.redmadrobot.flipper_plugin.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.debug_panel_common.base.PluginFragment
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.flipper_plugin.R
import com.redmadrobot.flipper_plugin.databinding.FragmentFlipperFeaturesBinding
import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
import com.redmadrobot.flipper_plugin.plugin.FlipperPluginContainer
import com.redmadrobot.flipper_plugin.ui.recycler.FlipperFeaturesAdapter
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
            onFeatureValueChanged = viewModel::onFeatureValueChanged,
        )

        binding.featureList.adapter = this.featuresAdapter
        binding.query.addTextChangedListener { text ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                featuresAdapter.submitList(state.featureItems)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }
}
