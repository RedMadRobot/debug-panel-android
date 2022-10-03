package com.redmadrobot.account_plugin.ui

import android.os.Bundle
import android.view.View
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.databinding.FragmentAccountsComposeBinding
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.account_plugin.plugin.AccountsPluginContainer
import com.redmadrobot.debug_panel_common.base.PluginFragment
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin

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
