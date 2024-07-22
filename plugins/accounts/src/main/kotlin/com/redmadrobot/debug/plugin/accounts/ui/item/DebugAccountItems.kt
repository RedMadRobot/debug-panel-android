package com.redmadrobot.debug.plugin.accounts.ui.item

import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

internal sealed class DebugAccountItems {
    internal data class Header(val header: String) : DebugAccountItems()
    internal data class PreinstalledAccount(val account: DebugAccount) : DebugAccountItems()
    internal data class AddedAccount(var account: DebugAccount) : DebugAccountItems()
}

