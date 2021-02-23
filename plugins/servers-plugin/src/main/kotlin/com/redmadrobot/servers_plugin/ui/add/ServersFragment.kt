package com.redmadrobot.servers_plugin.ui.add

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel_common.base.PluginFragment
import com.redmadrobot.debug_panel_common.extension.observe
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_common.ui.ItemTouchHelperCallback
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.plugin.ServersPlugin
import com.redmadrobot.servers_plugin.plugin.ServersPluginContainer
import com.redmadrobot.servers_plugin.ui.ServersViewState
import com.redmadrobot.servers_plugin.ui.item.DebugServerItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_add_server.*

internal class ServersFragment : PluginFragment(R.layout.fragment_add_server) {

    companion object {
        fun getInstance() = ServersFragment()
    }

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

        val itemTouchHelperCallback =
            ItemTouchHelperCallback(
                onSwiped = { position ->
                    /*remove server from DB*/
                    val item = serversAdapter.getItem(position) as DebugServerItem
                    serversViewModel.removeServer(item)
                },
                canBeSwiped = { position ->
                    serversAdapter.getGroupAtAdapterPosition(position) == addedServersSection &&
                            serversAdapter.getItem(position) is DebugServerItem
                }
            )

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(server_list)
        }

        add_server.setOnClickListener {
            ServerHostDialog.show(childFragmentManager)
        }

        serversAdapter.setOnItemClickListener { item, _ ->
            (item as? DebugServerItem)?.let { handleItemClick(it) }
        }

        serversAdapter.add(preInstalledServersSection)
        serversAdapter.add(addedServersSection)
    }

    private fun handleItemClick(item: DebugServerItem) {
        if (addedServersSection.getPosition(item) >= 0) {
            val debugServer = item.debugServer
            val bundle = Bundle().apply {
                putInt(ServerHostDialog.KEY_ID, debugServer.id)
                putString(ServerHostDialog.KEY_NAME, debugServer.name)
                putString(ServerHostDialog.KEY_URL, debugServer.url)
            }
            ServerHostDialog.show(childFragmentManager, bundle)
        }
    }

    private fun render(state: ServersViewState) {
        preInstalledServersSection.update(state.preInstalledItems)
        addedServersSection.update(state.addedItems)
    }
}
