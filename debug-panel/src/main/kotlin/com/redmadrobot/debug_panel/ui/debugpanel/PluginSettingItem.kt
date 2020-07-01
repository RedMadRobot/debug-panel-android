package com.redmadrobot.debug_panel.ui.debugpanel

import com.redmadrobot.debug_panel.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_debug_feature.view.*

class PluginSettingItem(
    private val pluginName: String,
    private val onClicked: () -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_debug_feature

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        containerView.item_debug_feature_label.text = pluginName
        containerView.setOnClickListener { onClicked.invoke() }
    }
}
