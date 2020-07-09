package com.redmadrobot.servers_plugin.ui.item

import androidx.core.view.isVisible
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_debug_server.view.*

class DebugServerItem(var debugServer: DebugServer, var isSelected: Boolean) : Item() {

    override fun getLayout() = R.layout.item_debug_server
    override fun getId() = debugServer.url.hashCode().toLong()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.containerView) {
            val url = debugServer.url.takeIf { url ->
                url.isNotEmpty()
            } ?: context.getString(R.string.default_server)

            item_server_url.text = url
            is_selected_icon.isVisible = this@DebugServerItem.isSelected
        }
    }

    fun update(debugServer: DebugServer) {
        this.debugServer = debugServer
        notifyChanged()
    }
}
