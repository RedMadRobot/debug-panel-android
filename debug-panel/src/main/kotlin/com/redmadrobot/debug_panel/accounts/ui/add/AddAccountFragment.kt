package com.redmadrobot.debug_panel.accounts.ui.add

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.accounts.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.AccountRepositoryProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.LocalAccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.ui.item.UserCredentialsItem
import com.redmadrobot.debug_panel.base.BaseFragment
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.view.ItemTouchHelperCallback
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_add_account.*

class AddAccountFragment : BaseFragment(R.layout.fragment_add_account),
    AddAccountDialog.SaveAccountResultListener {

    companion object {
        fun getInstance() = AddAccountFragment()
    }

    private val accountRepositoryProvider by lazy { AccountRepositoryProvider(requireContext()) }
    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        loadAccounts()
    }

    override fun onAccountSaved(login: String, password: String) {
        val userData = DebugUserCredentials(login, password)
        accountsAdapter.add(
            UserCredentialsItem(userData)
        )
        accountRepositoryProvider.getAccountRepository()
            .addCredential(userData)
            .subscribeBy()
            .autoDispose()
    }

    private fun setView() {
        account_list.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val itemTouchHelperCallback = ItemTouchHelperCallback(accountsAdapter)
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(account_list)
        }

        add_account.setOnClickListener {
            AddAccountDialog.show(childFragmentManager, this)
        }
    }

    private fun loadAccounts() {
        val accountRepository = accountRepositoryProvider.getAccountRepository()
        val credentialsProvider = AccountsProvider(LocalAccountsLoadStrategy(accountRepository))
        credentialsProvider.getAccounts()
            .observeOnMain()
            .map { it.map(::UserCredentialsItem) }
            .subscribeBy(onSuccess = { accountsAdapter.update(it) })
            .autoDispose()
    }

}
