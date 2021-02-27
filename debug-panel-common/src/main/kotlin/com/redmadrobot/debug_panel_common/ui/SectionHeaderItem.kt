package com.redmadrobot.debug_panel_common.ui

import com.redmadrobot.debug_panel_common.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_section_header.view.*

public class SectionHeaderItem(private val title: String) : Item() {

    override fun getLayout(): Int = R.layout.item_section_header

    override fun getId(): Long = title.hashCode().toLong()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.containerView.item_section_title.text = title
    }
}
