package com.redmadrobot.account_plugin.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.DebugAccountRepository
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.ui.item.AccountItem
import com.redmadrobot.debug_panel_common.extension.safeLaunch
import com.redmadrobot.debug_panel_core.ui.SectionHeaderItem
import com.xwray.groupie.kotlinandroidextensions.Item

internal class AccountsViewModel(
    private val context: Context,
    private val debugAccountsRepository: DebugAccountRepository
) : ViewModel() {

    val state = MutableLiveData<AccountsViewState>().apply {
        /*Default state*/
        value = AccountsViewState(
            preInstalledItems = emptyList(),
            addedItems = emptyList()
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
        val account = DebugAccount(
            id = id,
            login = newLogin,
            password = newPassword,
            pin = pin
        )
        viewModelScope.safeLaunch {
            debugAccountsRepository.updateAccount(account)
            getItemById(id)?.update(account)
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
        if (accounts.isNotEmpty()) {
            val items = mapToItems(context.getString(R.string.pre_installed_accounts), accounts)
            state.value = state.value?.copy(preInstalledItems = items)
        }
    }

    private suspend fun loadAddedAccounts() {
        val accounts = debugAccountsRepository.getAccounts()
        if (accounts.isNotEmpty()) {
            val items = mapToItems(context.getString(R.string.added_accounts), accounts)
            state.value = state.value?.copy(addedItems = items)
        }
    }

    private fun mapToItems(header: String, accounts: List<DebugAccount>): List<Item> {
        val items = accounts.map { account ->
            AccountItem(account)
        }
        return listOf(/*Header item*/SectionHeaderItem(header))
            .plus(items)
    }

    private fun getItemById(id: Int): AccountItem? {
        return state.value?.addedItems
            ?.find { item ->
                item is AccountItem && item.account.id == id
            } as? AccountItem
    }
}
