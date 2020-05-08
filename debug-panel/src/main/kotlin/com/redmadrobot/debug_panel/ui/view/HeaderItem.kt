package com.redmadrobot.debug_panel.ui.view

import com.redmadrobot.debug_panel.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_header.view.*

class HeaderItem(private val headerText: String) : Item() {

    override fun getLayout() = R.layout.item_header

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.containerView.item_header_text.text = headerText
    }
}
