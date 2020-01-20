package com.redmadrobot.debug_panel.accounts.ui.choose

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.accounts.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.AccountRepositoryProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.LocalAccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.PreinstalledAccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.ui.item.UserCredentialsItem
import com.redmadrobot.debug_panel.extension.autoDispose
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_account_select.*

class AccountSelectFragment : Fragment(R.layout.fragment_account_select) {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private lateinit var accountRepositoryProvider: AccountRepositoryProvider

    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountRepositoryProvider = AccountRepositoryProvider(requireContext())

        setView()
        loadMockData()
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    private fun setView() {
        account_select_recycler.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        accountsAdapter.setOnItemClickListener { item, _ ->
            val userCredential = (item as? UserCredentialsItem)?.userCredentials
            userCredential?.let(::sendUserCredentials)
        }
    }

    private fun sendUserCredentials(userCredentials: DebugUserCredentials) {
        (targetFragment as? AccountDataResultListener)?.onAccountSelected(
            userCredentials.login,
            userCredentials.password
        )
    }

    private fun loadMockData() {
        val accountRepository = accountRepositoryProvider.getAccountRepository()
        val accountsProvider = AccountsProvider(LocalAccountsLoadStrategy(accountRepository))
        val preInstalledAccountsProvider = AccountsProvider(PreinstalledAccountsLoadStrategy())

        accountsProvider.getAccounts()
            .mergeWith(preInstalledAccountsProvider.getAccounts())
            .firstOrError()
            .observeOnMain()
            .map { it.map(::UserCredentialsItem) }
            .subscribeBy(onSuccess = { accountsAdapter.update(it) })
            .autoDispose(compositeDisposable)
    }

    interface AccountDataResultListener {
        fun onAccountSelected(login: String, password: String)
    }
}
