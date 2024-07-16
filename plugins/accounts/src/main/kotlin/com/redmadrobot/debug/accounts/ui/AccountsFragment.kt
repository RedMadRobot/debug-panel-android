package com.redmadrobot.debug.accounts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.accounts.plugin.AccountsPlugin
import com.redmadrobot.debug.accounts.plugin.AccountsPluginContainer
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin

internal class AccountsFragment : Fragment() {

    private val isSettingMode: Boolean by lazy {
        activity?.javaClass?.simpleName == "DebugActivity"
    }

    companion object {
        const val IS_EDIT_MODE_KEY = "IS_EDIT_MODE_KEY"
    }

    private val viewModel by lazy {
        obtainShareViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(inflater.context).apply {
        setContent {
            AccountsScreen(viewModel, isSettingMode)
        }
    }
}
