package com.redmadrobot.feature_togles_plugin.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.core.extension.getPlugin
import com.redmadrobot.core.extension.observe
import com.redmadrobot.core.extension.obtainViewModel
import com.redmadrobot.core.ui.base.BaseFragment
import com.redmadrobot.feature_toggles_plugin.R
import com.redmadrobot.feature_togles_plugin.plugin.FeatureTogglesPlugin
import com.redmadrobot.feature_togles_plugin.plugin.FeatureTogglesPluginContainer
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_feature_toggles.*

class FeatureTogglesFragment : BaseFragment(R.layout.fragment_feature_toggles) {

    private val featureTogglesViewModel by lazy {
        obtainViewModel {
            getPlugin<FeatureTogglesPlugin>()
                .getContainer<FeatureTogglesPluginContainer>()
                .createFeatureTogglesViewModel()
        }
    }

    private val featureTogglesAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(featureTogglesViewModel.screenState, ::render)
        featureTogglesViewModel.loadFeatureToggles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun render(screenState: FeatureTogglesState) {
        feature_toggles_override_enable.isChecked = screenState.overrideEnable
        featureTogglesAdapter.update(screenState.featureToggleItems)
    }

    private fun setView() {
        feature_toggles_recycler.apply {
            adapter = featureTogglesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        feature_toggles_reset_all.setOnClickListener { featureTogglesViewModel.resetAll() }
        feature_toggles_override_enable.setOnCheckedChangeListener { _, isChecked ->
            featureTogglesViewModel.updateOverrideEnable(isChecked)
        }
    }
}
