package com.redmadrobot.debug_panel.ui.accounts.item

import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_user_credentials.view.*

class AccountItem(val account: DebugAccount) : Item() {

    override fun getLayout() = R.layout.item_user_credentials
    override fun getId() = R.layout.item_user_credentials.toLong()


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.item_credential_login.text = account.login
    }
}
