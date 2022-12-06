package com.redmadrobot.debug.account.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.ui.item.DebugAccountItems
import com.redmadrobot.debug.account.data.DebugAccountRepository
import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug.common.base.PluginViewModel
import com.redmadrobot.debug.common.extension.safeLaunch

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

    fun saveAccount(login: String, password: String, pin: String) {
        val account = DebugAccount(
            login = login,
            password = password,
            pin = pin
        )
        viewModelScope.safeLaunch {
            debugAccountsRepository.addAccount(account)
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

    private suspend fun loadPreInstalledAccounts() {
        val accounts = debugAccountsRepository.getPreInstalledAccounts()
        val preInstalledAccounts = if (accounts.isNotEmpty()) {
            val items = accounts.map { account ->
                DebugAccountItems.PreinstalledAccount(account)
            }
            val header = context.getString(R.string.pre_installed_accounts)
            listOf(/*Header item*/DebugAccountItems.Header(header)).plus(items)
        } else {
            emptyList()
        }
        state.value = state.value?.copy(preInstalledAccounts = preInstalledAccounts)
    }

    private suspend fun loadAddedAccounts() {
        val accounts = debugAccountsRepository.getAccounts()
        val addedAccountItems = if (accounts.isNotEmpty()) {
            val items = accounts.map { account ->
                DebugAccountItems.AddedAccount(account)
            }
            val header = context.getString(R.string.added_accounts)
            listOf(/*Header item*/DebugAccountItems.Header(header)).plus(items)
        } else {
            emptyList()
        }
        state.value = state.value?.copy(addedAccounts = addedAccountItems)
    }
}
