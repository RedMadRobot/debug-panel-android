package com.redmadrobot.debug_panel.internal.plugin.account

import androidx.fragment.app.Fragment
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.Plugin
import com.redmadrobot.core.PluginDependencyContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.accounts.DefaultAuthenticator
import com.redmadrobot.debug_panel.ui.accounts.add.AccountsFragment
import com.redmadrobot.debug_panel.ui.accounts.select.AccountSelectionFragment

class AccountsPlugin(
    private val preInstalledAccounts: PreInstalledData<DebugAccount> = PreInstalledData(
        emptyList()
    ),
    val authenticator: Authenticator = DefaultAuthenticator()
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
