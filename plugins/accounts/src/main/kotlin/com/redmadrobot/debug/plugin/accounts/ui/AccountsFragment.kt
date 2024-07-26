package com.redmadrobot.debug.plugin.accounts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.isSettingMode
import com.redmadrobot.debug.core.extension.obtainShareViewModel
import com.redmadrobot.debug.plugin.accounts.AccountsPlugin
import com.redmadrobot.debug.plugin.accounts.AccountsPluginContainer

internal class AccountsFragment : Fragment() {
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
            AccountsScreen(
                viewModel = viewModel,
                isEditMode = this@AccountsFragment.isSettingMode()
            )
        }
    }
}
