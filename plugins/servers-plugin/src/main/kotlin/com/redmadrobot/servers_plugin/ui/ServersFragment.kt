package com.redmadrobot.servers_plugin.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel_common.base.PluginFragment
import com.redmadrobot.debug_panel_common.databinding.ItemSectionHeaderBinding
import com.redmadrobot.debug_panel_common.extension.observe
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.itemsadapter.ItemsAdapter
import com.redmadrobot.itemsadapter.bind
import com.redmadrobot.itemsadapter.itemsAdapter
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.databinding.ItemDebugServerBinding
import com.redmadrobot.servers_plugin.plugin.ServersPlugin
import com.redmadrobot.servers_plugin.plugin.ServersPluginContainer
import com.redmadrobot.servers_plugin.ui.item.DebugServerItems
import kotlinx.android.synthetic.main.fragment_servers.*

internal class ServersFragment : PluginFragment(R.layout.fragment_servers) {

    companion object {
        const val IS_EDIT_MODE_KEY = "IS_EDIT_MODE_KEY"
    }

    private val isEditMode by lazy {
        requireNotNull(arguments).getBoolean(IS_EDIT_MODE_KEY)
    }

    private val viewModel by lazy {
        obtainShareViewModel {
            getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .createServersViewModel()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(viewModel.state, ::render)
        viewModel.loadServers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    private fun setViews() {
        server_list.layoutManager = LinearLayoutManager(requireContext())
        add_server.setOnClickListener {
            ServerHostDialog.show(childFragmentManager)
        }
    }

    private fun render(state: ServersViewState) {
        val adapter = createAdapterByState(state)
        server_list.adapter = adapter
    }

    private fun createAdapterByState(state: ServersViewState): ItemsAdapter {
        val items = state.preInstalledServers.plus(state.addedServers)
        return itemsAdapter(items) { item ->
            when (item) {
                is DebugServerItems.Header -> {
                    bind<ItemSectionHeaderBinding>(R.layout.item_section_header) {
                        itemSectionTitle.text = item.header
                    }
                }
                is DebugServerItems.PreinstalledServer -> {
                    bind<ItemDebugServerBinding>(R.layout.item_debug_server) {
                        itemServerName.text = item.debugServer.name
                        isSelectedIcon.isVisible = item.isSelected && !isEditMode
                        if (!isEditMode) {
                            root.setOnClickListener {
                                viewModel.selectServerAsCurrent(item.debugServer)
                            }
                        }
                    }
                }
                is DebugServerItems.AddedServer -> {
                    bind<ItemDebugServerBinding>(R.layout.item_debug_server) {
                        itemServerName.text = item.debugServer.name
                        isSelectedIcon.isVisible = item.isSelected && !isEditMode
                        itemServerDelete.isVisible = isEditMode
                        val server = item.debugServer
                        itemServerDelete.setOnClickListener { viewModel.removeServer(server) }
                        root.setOnClickListener {
                            if (!isEditMode) {
                                viewModel.selectServerAsCurrent(server)
                            } else {
                                editServerData(server)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun editServerData(debugServer: DebugServer) {
        val bundle = Bundle().apply {
            putInt(ServerHostDialog.KEY_ID, debugServer.id)
            putString(ServerHostDialog.KEY_NAME, debugServer.name)
            putString(ServerHostDialog.KEY_URL, debugServer.url)
        }
        ServerHostDialog.show(childFragmentManager, bundle)
    }
}
