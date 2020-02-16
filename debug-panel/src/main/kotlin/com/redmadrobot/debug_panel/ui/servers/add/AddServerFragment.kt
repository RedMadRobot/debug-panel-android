package com.redmadrobot.debug_panel.ui.servers.add

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.extension.observe
import com.redmadrobot.debug_panel.extension.obtainViewModel
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.ui.base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_add_server.*

class AddServerFragment : BaseFragment(R.layout.fragment_add_server) {

    companion object {
        fun getInstance() = AddServerFragment()
    }

    private val serversAdapter = GroupAdapter<GroupieViewHolder>()

    private val serversViewModel by lazy {
        obtainViewModel {
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
    }

    private fun setServerList(servers: List<Item>) {
        serversAdapter.update(servers)
    }
}
