package com.redmadrobot.account_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.account_plugin.authenticator.DebugAuthenticator
import com.redmadrobot.account_plugin.authenticator.DefaultAuthenticator
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.ui.add.AccountsFragment
import com.redmadrobot.account_plugin.ui.select.AccountSelectionFragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer

class AccountsPlugin(
    private val preInstalledAccounts: List<DebugAccount> = emptyList(),
    val debugAuthenticator: DebugAuthenticator = DefaultAuthenticator()
) : Plugin() {

    companion object {
        const val NAME = "ACCOUNTS"
    }

    override fun getName() = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AccountsPluginContainer(preInstalledAccounts, commonContainer)
    }

    override fun getFragment(): Fragment? {
        return AccountSelectionFragment()
    }

    override fun getSettingFragment(): Fragment? {
        return AccountsFragment()
    }
}
