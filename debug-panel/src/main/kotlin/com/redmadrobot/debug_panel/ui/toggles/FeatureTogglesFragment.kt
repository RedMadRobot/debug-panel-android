package com.redmadrobot.debug_panel.ui.toggles

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.extension.observe
import com.redmadrobot.debug_panel.extension.obtainViewModel
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.ui.base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_feature_toggles.*

internal class FeatureTogglesFragment : BaseFragment(R.layout.fragment_feature_toggles) {
    private val featureTogglesViewModel by lazy {
        obtainViewModel {
            DebugPanel.getContainer().createFeatureTogglesViewModel()
        }
    }

    private val featureTogglesAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(featureTogglesViewModel.featureToggleItems, ::setFeatureToggles)
        featureTogglesViewModel.loadFeatureToggles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
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
    }

    private fun setFeatureToggles(accounts: List<Item>) {
        featureTogglesAdapter.clear()
        featureTogglesAdapter.addAll(accounts)
    }
}
