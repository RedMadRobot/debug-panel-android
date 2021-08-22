package com.redmadrobot.account_plugin.plugin

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.redmadrobot.account_plugin.authenticator.DebugAuthenticator
import com.redmadrobot.account_plugin.authenticator.DefaultAuthenticator
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.ui.AccountsFragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer

public class AccountsPlugin(
    private val preInstalledAccounts: List<DebugAccount> = emptyList(),
    public val debugAuthenticator: DebugAuthenticator = DefaultAuthenticator()
) : Plugin() {

    internal companion object {
        const val NAME = "ACCOUNTS"
    }

    public constructor(
        preInstalledAccounts: DebugDataProvider<List<DebugAccount>>,
        debugAuthenticator: DebugAuthenticator = DefaultAuthenticator()
    ) : this(
        preInstalledAccounts = preInstalledAccounts.provideData(),
        debugAuthenticator = debugAuthenticator
    )

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AccountsPluginContainer(preInstalledAccounts, commonContainer)
    }

    override fun getFragment(): Fragment {
        return AccountsFragment().apply {
            arguments = bundleOf(AccountsFragment.IS_EDIT_MODE_KEY to false)
        }
    }

    override fun getSettingFragment(): Fragment {
        return AccountsFragment().apply {
            arguments = bundleOf(AccountsFragment.IS_EDIT_MODE_KEY to true)
        }
    }
}
