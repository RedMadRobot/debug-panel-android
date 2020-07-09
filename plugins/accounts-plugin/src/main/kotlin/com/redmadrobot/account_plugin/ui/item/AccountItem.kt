package com.redmadrobot.account_plugin.ui.item

import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_user_credentials.view.*

class AccountItem(var account: DebugAccount) : Item() {

    override fun getLayout() = R.layout.item_user_credentials
    override fun getId() = account.id.toLong()

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.item_credential_login.text = account.login
    }

    fun update(account: DebugAccount) {
        this.account = account
        notifyChanged()
    }
}
