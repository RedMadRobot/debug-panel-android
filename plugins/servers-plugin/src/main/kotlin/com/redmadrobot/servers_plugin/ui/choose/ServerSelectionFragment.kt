package com.redmadrobot.servers_plugin.ui.choose

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel_common.base.BaseFragment
import com.redmadrobot.debug_panel_common.extension.observe
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.plugin.ServerSelectedEvent
import com.redmadrobot.servers_plugin.plugin.ServersPlugin
import com.redmadrobot.servers_plugin.plugin.ServersPluginContainer
import com.redmadrobot.servers_plugin.ui.ServersViewState
import com.redmadrobot.servers_plugin.ui.item.DebugServerItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_add_server.*

internal class ServerSelectionFragment : BaseFragment(R.layout.fragment_server_selection) {

    private val serversAdapter = GroupAdapter<GroupieViewHolder>()
    private val preInstalledServersSection = Section()
    private val addedServersSection = Section()

    private val serversViewModel by lazy {
        obtainShareViewModel {
            getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .createServersViewModel()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(serversViewModel.state, ::render)
        serversViewModel.loadServers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        server_list.layoutManager = LinearLayoutManager(requireContext())
        server_list.adapter = serversAdapter

        serversAdapter.setOnItemClickListener { item, _ ->
            (item as? DebugServerItem)?.let { onServerSelected(it) }
        }

        serversAdapter.add(preInstalledServersSection)
        serversAdapter.add(addedServersSection)
    }

    private fun render(state: ServersViewState) {
        preInstalledServersSection.update(state.preInstalledItems)
        addedServersSection.update(state.addedItems)
    }

    private fun onServerSelected(debugServerItem: DebugServerItem) {
        serversViewModel.selectServerAsCurrent(debugServerItem)
        getPlugin<ServersPlugin>().pushEvent(ServerSelectedEvent(debugServerItem.debugServer))
    }
}
