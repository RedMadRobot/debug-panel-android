package com.redmadrobot.account_plugin.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.DebugAccountRepository
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.ui.item.AccountItem
import com.redmadrobot.core.extension.observeOnMain
import com.redmadrobot.core.ui.SectionHeaderItem
import com.redmadrobot.core.ui.base.BaseViewModel
import com.xwray.groupie.kotlinandroidextensions.Item
import timber.log.Timber

class AccountsViewModel(
    private val context: Context,
    private val debugAccountsRepository: DebugAccountRepository
) : BaseViewModel() {

    val state = MutableLiveData<AccountsViewState>().apply {
        /*Default state*/
        value = AccountsViewState(
            preInstalledItems = emptyList(),
            addedItems = emptyList()
        )
    }

    fun loadAccounts() {
        loadPreInstalledAccounts()
        loadAddedAccounts()
    }

    fun saveAccount(login: String, password: String, pinNeeded: Boolean) {
        val account = DebugAccount(
            login = login,
            password = password,
            pinNeeded = pinNeeded
        )
        debugAccountsRepository
            .addAccount(account)
            .observeOnMain()
            .subscribe { loadAddedAccounts() }
            .autoDispose()
    }

    fun updateAccount(
        id: Int,
        newLogin: String,
        newPassword: String,
        pinNeeded: Boolean
    ) {
        val account = DebugAccount(
            id = id,
            login = newLogin,
            password = newPassword,
            pinNeeded = pinNeeded
        )
        debugAccountsRepository
            .updateAccount(account)
            .observeOnMain()
            .subscribe(
                { getItemById(id)?.update(account) },
                { Timber.e(it) }
            )
            .autoDispose()
    }


    fun removeAccount(account: DebugAccount) {
        debugAccountsRepository
            .removeAccount(account)
            .observeOnMain()
            .subscribe(
                { loadAddedAccounts() },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    private fun loadPreInstalledAccounts() {
        debugAccountsRepository.getPreInstalledAccounts()
            .filter { it.isNotEmpty() }
            .map { accounts ->
                /*"Pre-installed" header*/
                listOf(SectionHeaderItem(context.getString(R.string.pre_installed_accounts)))
                    .plus(mapToAccountItems(accounts))
            }
            .observeOnMain()
            .subscribe(
                { items ->
                    state.value = state.value?.copy(preInstalledItems = items)
                },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    private fun loadAddedAccounts() {
        debugAccountsRepository.getAccounts()
            .filter { it.isNotEmpty() }
            .map { accounts ->
                /*"Added" header*/
                listOf(SectionHeaderItem(context.getString(R.string.added_accounts)))
                    .plus(mapToAccountItems(accounts))
            }
            .observeOnMain()
            .subscribe(
                { items ->
                    state.value = state.value?.copy(addedItems = items)
                },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    private fun mapToAccountItems(accounts: List<DebugAccount>): List<Item> {
        return accounts.map { account ->
            AccountItem(account)
        }
    }

    private fun getItemById(id: Int): AccountItem? {
        return state.value?.addedItems
            ?.find { item ->
                item is AccountItem && item.account.id == id
            } as? AccountItem
    }
}
