package com.redmadrobot.debug_panel.ui.servers.choose

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.extension.observe
import com.redmadrobot.debug_panel.extension.obtainShareViewModel
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.ui.base.BaseFragment
import com.redmadrobot.debug_panel.ui.servers.item.DebugServerItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_add_server.*

class ServerSelectionFragment : BaseFragment(R.layout.fragment_server_selection) {

    private val serversAdapter = GroupAdapter<GroupieViewHolder>()

    private val serversViewModel by lazy {
        obtainShareViewModel {
            DebugPanel.getContainer().createServersViewModel()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(serversViewModel.servers, ::setServerList)
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
            val serverData = (item as DebugServerItem).debugServer
            serversViewModel.selectServerAsCurrent(serverData)
        }
    }

    private fun setServerList(servers: List<Item>) {
        serversAdapter.update(servers)
    }
}
