package com.redmadrobot.debug_panel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.accounts.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.AccountRepositoryProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.LocalAccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.ui.add.AddAccountDialog
import com.redmadrobot.debug_panel.accounts.ui.item.UserCredentialsItem
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.view.ItemTouchHelperCallback
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : AppCompatActivity(), AddAccountDialog.SaveAccountResultListener {

    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val accountRepositoryProvider = AccountRepositoryProvider(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_debug)
        setView()
        loadAccounts()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onAccountSaved(login: String, password: String) {
        val userData = DebugUserCredentials(login, password)
        accountsAdapter.add(
            UserCredentialsItem(userData)
        )
        accountRepositoryProvider.getAccountRepository()
            .addCredential(userData)
            .subscribeBy()
            .also { compositeDisposable.add(it) }
    }

    private fun setView() {
        debug_activity_account_list.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(this@DebugActivity)
        }

        val itemTouchHelperCallback = ItemTouchHelperCallback(accountsAdapter)
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(debug_activity_account_list)
        }

        debug_activity_add_account_button.setOnClickListener {
            AddAccountDialog.show(supportFragmentManager)
        }
    }

    private fun loadAccounts() {
        val accountRepository = accountRepositoryProvider.getAccountRepository()
        val credentialsProvider = AccountsProvider(LocalAccountsLoadStrategy(accountRepository))
        credentialsProvider.getAccounts()
            .observeOnMain()
            .map { it.map(::UserCredentialsItem) }
            .subscribeBy(onSuccess = { accountsAdapter.update(it) })
            .also { compositeDisposable?.add(it) }
    }
}
