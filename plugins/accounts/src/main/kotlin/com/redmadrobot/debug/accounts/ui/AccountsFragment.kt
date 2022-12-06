package com.redmadrobot.debug.accounts.ui

import android.os.Bundle
import android.view.View
import com.redmadrobot.account.R
import com.redmadrobot.account.databinding.FragmentAccountsComposeBinding
import com.redmadrobot.debug.accounts.plugin.AccountsPlugin
import com.redmadrobot.debug.accounts.plugin.AccountsPluginContainer
import com.redmadrobot.debug.common.base.PluginFragment
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin

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