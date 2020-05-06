package com.redmadrobot.debug_panel.ui.accounts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.data.accounts.DebugAccountRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.ui.accounts.item.AccountItem
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.view.SectionHeaderItem
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.rxkotlin.subscribeBy

class AccountsViewModel(
    private val context: Context,
    private val debugAccountsRepository: DebugAccountRepository
) : BaseViewModel() {

    val state = MutableLiveData<AccountsViewState>().apply {
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
            .map { accounts ->
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
            .map { accounts ->
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
