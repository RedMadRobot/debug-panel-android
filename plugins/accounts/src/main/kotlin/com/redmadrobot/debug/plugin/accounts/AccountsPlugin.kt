package com.redmadrobot.debug.plugin.accounts

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.plugin.accounts.authenticator.DebugAuthenticator
import com.redmadrobot.debug.plugin.accounts.authenticator.DefaultAuthenticator
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
import com.redmadrobot.debug.plugin.accounts.ui.AccountsFragment
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer

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

    @Composable
    override fun content() {
        AndroidViewBinding(factory = FragmentContainerAccountBinding::inflate) {
            fragmentContainer.getFragment<AccountsFragment>().apply {
                arguments = bundleOf(AccountsFragment.IS_EDIT_MODE_KEY to false)
            }
        }
    }

    @Composable
    override fun settingsContent() {
        AndroidViewBinding(factory = FragmentContainerAccountBinding::inflate) {
            fragmentContainer.getFragment<AccountsFragment>().apply {
                arguments = bundleOf(AccountsFragment.IS_EDIT_MODE_KEY to true)
            }
        }
    }

    override fun getSettingFragment(): Fragment {
        return AccountsFragment().apply {
            arguments = bundleOf(AccountsFragment.IS_EDIT_MODE_KEY to true)
        }
    }
}
