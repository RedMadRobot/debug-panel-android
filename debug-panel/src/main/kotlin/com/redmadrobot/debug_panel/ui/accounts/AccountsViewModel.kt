package com.redmadrobot.debug_panel.ui.accounts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.core.data.accounts.DebugAccountRepository
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.core.extension.observeOnMain
import com.redmadrobot.core.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.ui.accounts.item.AccountItem
import com.redmadrobot.debug_panel.ui.view.SectionHeaderItem
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.rxkotlin.subscribeBy

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

    fun saveAccount(login: String, password: String) {
        val account = DebugAccount(
            login = login,
            password = password
        )
        debugAccountsRepository
            .addAccount(account)
            .observeOnMain()
            .subscribeBy(
                onComplete = { loadAddedAccounts() }
            )
            .autoDispose()
    }

    fun updateAccount(id: Int, newLogin: String, newPassword: String) {
        val account = DebugAccount(
            id = id,
            login = newLogin,
            password = newPassword
        )
        debugAccountsRepository
            .updateAccount(account)
            .observeOnMain()
            .subscribeBy(
                onComplete = { getItemById(id)?.update(account) }
            )
            .autoDispose()
    }


    fun removeAccount(account: DebugAccount) {
        debugAccountsRepository
            .removeAccount(account)
            .observeOnMain()
            .subscribeBy(
                onComplete = { loadAddedAccounts() }
            )
            .autoDispose()
    }

    private fun loadPreInstalledAccounts() {
        debugAccountsRepository.getPreInstalledAccounts()
            .filter { it.isNotEmpty() }
            .map { accounts ->
                /*"Pre-installed" header*/
                listOf(SectionHeaderItem(context.getString(R.string.pre_installed)))
                    .plus(mapToAccountItems(accounts))
            }
            .observeOnMain()
            .subscribeBy(onSuccess = { items ->
                state.value = state.value?.copy(preInstalledItems = items)
            })
            .autoDispose()
    }

    private fun loadAddedAccounts() {
        debugAccountsRepository.getAccounts()
            .filter { it.isNotEmpty() }
            .map { accounts ->
                /*"Added" header*/
                listOf(SectionHeaderItem(context.getString(R.string.added)))
                    .plus(mapToAccountItems(accounts))
            }
            .observeOnMain()
            .subscribeBy(onSuccess = { items ->
                state.value = state.value?.copy(addedItems = items)
            })
            .autoDispose()
    }

    private fun mapToAccountItems(accounts: List<DebugAccount>): List<Item> {
        return accounts.map { AccountItem(it) }
    }

    private fun getItemById(id: Int): AccountItem? {
        return state.value?.addedItems
            ?.find { item ->
                item is AccountItem && item.account.id == id
            } as? AccountItem
    }
}
