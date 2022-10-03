package com.redmadrobot.debug.plugin.accounts.ui

import android.os.Bundle
import android.view.View
import com.redmadrobot.account_plugin.ui.AccountsScreen
import com.redmadrobot.debug.common.base.PluginFragment
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.accounts.AccountsPlugin
import com.redmadrobot.debug.plugin.accounts.AccountsPluginContainer
import com.redmadrobot.debug.plugin.accounts.R
import com.redmadrobot.debug.plugin.accounts.databinding.FragmentAccountsComposeBinding

internal class AccountsFragment : PluginFragment(R.layout.fragment_accounts_compose) {

    companion object {
        const val IS_EDIT_MODE_KEY = "IS_EDIT_MODE_KEY"
    }

    private val isEditMode by lazy {
        requireNotNull(arguments).getBoolean(IS_EDIT_MODE_KEY)
    }

    private val viewModel by lazy {
        obtainShareViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAccountsComposeBinding.bind(view)
        binding.composeRoot.setContent {
            AccountsScreen(viewModel, isEditMode)
        }
    }
}
