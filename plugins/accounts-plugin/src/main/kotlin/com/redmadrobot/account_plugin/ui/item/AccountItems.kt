package com.redmadrobot.account_plugin.ui.item

import com.redmadrobot.account_plugin.data.model.DebugAccount


internal sealed class AccountItems {
    internal data class Header(val header: String) : AccountItems()
    internal data class PreinstalledAccount(val account: DebugAccount) : AccountItems()
    internal data class AddedAccount(var account: DebugAccount) : AccountItems()
}

