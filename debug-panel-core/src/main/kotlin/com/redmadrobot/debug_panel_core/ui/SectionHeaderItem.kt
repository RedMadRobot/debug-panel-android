package com.redmadrobot.debug_panel_core.ui

import com.redmadrobot.panel_core.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_section_header.view.*

class SectionHeaderItem(private val title: String) : Item() {

    override fun getLayout() = R.layout.item_section_header
    override fun getId() = title.hashCode().toLong()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.containerView.item_section_title.text = title
    }
}
