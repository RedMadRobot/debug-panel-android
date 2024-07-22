package com.redmadrobot.debug.plugin.variable.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.variable.VariablePlugin
import com.redmadrobot.debug.plugin.variable.VariablePluginContainer
import com.redmadrobot.debug.plugin.variable.ui.recycler.VariableAdapter
import com.redmadrobot.debug.plugin.variable.R
import com.redmadrobot.debug.plugin.variable.databinding.FragmentVariableBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class VariableFragment : Fragment(R.layout.fragment_variable) {

    private var _binding: FragmentVariableBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VariableViewModel by lazy {
        obtainShareViewModel {
            getPlugin<VariablePlugin>()
                .getContainer<VariablePluginContainer>()
                .createVariableViewModel()
        }
    }

    private lateinit var adapter: VariableAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentVariableBinding.bind(view)

        adapter = VariableAdapter(
            onEventAction = viewModel::onVariableEvent,
            onWidgetRequested = viewModel::requireVariableWidget,
            onItemSettingsRequested = viewModel::requireVariableWidgetSettings,
            onItemEnabledStatusRequested = viewModel::getVariableEnabledStatus,
        )

        binding.recycler.adapter = adapter

        observeData()
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    private fun observeData() {
        viewModel.modifiers
            .onEach { values ->
                adapter.submitList(values)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
