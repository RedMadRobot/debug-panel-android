package com.redmadrobot.account_plugin.ui.item

import com.redmadrobot.account_plugin.data.model.DebugAccount


internal sealed class DebugAccountItems {
    internal data class Header(val header: String) : DebugAccountItems()
    internal data class PreinstalledAccount(val account: DebugAccount) : DebugAccountItems()
    internal data class AddedAccount(var account: DebugAccount) : DebugAccountItems()
}

