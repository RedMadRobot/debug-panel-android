package com.redmadrobot.debug.account.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.account.R
import com.redmadrobot.debug.account.data.DebugAccountRepository
import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug.account.plugin.AccountSelectedEvent
import com.redmadrobot.debug.account.plugin.AccountsPlugin
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_common.extension.safeLaunch
import com.redmadrobot.debug_panel_core.extension.getPlugin

internal class AccountsViewModel(
    private val context: Context,
    private val debugAccountsRepository: DebugAccountRepository
) : PluginViewModel() {

    val state = MutableLiveData<AccountsViewState>().apply {
        /*Default state*/
        value = AccountsViewState(
            preInstalledAccounts = emptyList(),
            addedAccounts = emptyList()
        )
    }

    fun loadAccounts() {
        viewModelScope.safeLaunch {
            loadPreInstalledAccounts()
            loadAddedAccounts()
        }
    }

    fun saveAccount(debugAccount: DebugAccount) {
        viewModelScope.safeLaunch {
            if (debugAccount.id != 0) {
                debugAccountsRepository.updateAccount(debugAccount)
            } else {
                debugAccountsRepository.addAccount(debugAccount)
            }
            loadAddedAccounts()
        }
    }

    fun updateAccount(
        id: Int,
        newLogin: String,
        newPassword: String,
        pin: String
    ) {
        val newAccount = DebugAccount(
            id = id,
            login = newLogin,
            password = newPassword,
            pin = pin
        )
        viewModelScope.safeLaunch {
            debugAccountsRepository.updateAccount(newAccount)
            loadAddedAccounts()
        }

    }

    fun removeAccount(account: DebugAccount) {
        viewModelScope.safeLaunch {
            debugAccountsRepository.removeAccount(account)
            loadAddedAccounts()
        }
    }

    fun setAccountAsCurrent(account: DebugAccount) {
        getPlugin<AccountsPlugin>().apply {
            debugAuthenticator.onAccountSelected(account)
            pushEvent(AccountSelectedEvent(account))
        }
    }

    private suspend fun loadPreInstalledAccounts() {
        val accounts = debugAccountsRepository.getPreInstalledAccounts()
        val preInstalledAccounts = if (accounts.isNotEmpty()) {
            val items = accounts.map { account ->
                DebugAccountItem.PreinstalledAccount(account)
            }
            val header = context.getString(R.string.pre_installed_accounts)
            listOf(/*Header item*/DebugAccountItem.Header(header)).plus(items)
        } else {
            emptyList()
        }
        state.value = state.value?.copy(preInstalledAccounts = preInstalledAccounts)
    }

    private suspend fun loadAddedAccounts() {
        val accounts = debugAccountsRepository.getAccounts()
        val addedAccountItems = if (accounts.isNotEmpty()) {
            val items = accounts.map { account ->
                DebugAccountItem.AddedAccount(account)
            }
            val header = context.getString(R.string.added_accounts)
            listOf(/*Header item*/DebugAccountItem.Header(header)).plus(items)
        } else {
            emptyList()
        }
        state.value = state.value?.copy(addedAccounts = addedAccountItems)
    }
}
