package com.redmadrobot.debug_panel.ui.accounts.select

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.core.extension.getPlugin
import com.redmadrobot.core.extension.observe
import com.redmadrobot.core.extension.obtainViewModel
import com.redmadrobot.core.ui.base.BaseFragment
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.internal.plugin.account.AccountsPlugin
import com.redmadrobot.debug_panel.internal.plugin.account.AccountsPluginContainer
import com.redmadrobot.debug_panel.ui.accounts.AccountsViewState
import com.redmadrobot.debug_panel.ui.accounts.item.AccountItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_account_select.*

class AccountSelectionFragment : BaseFragment(R.layout.fragment_account_select) {

    private val accountsViewModel by lazy {
        obtainViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }

    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()
    private val preInstalledAccountsSection = Section()
    private val addedAccountsSection = Section()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(accountsViewModel.state, ::render)
        accountsViewModel.loadAccounts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }


    private fun setView() {
        account_select_recycler.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        accountsAdapter.setOnItemClickListener { item, _ ->
            val account = (item as? AccountItem)?.account
            account?.let(::selectAccount)
        }
        accountsAdapter.add(preInstalledAccountsSection)
        accountsAdapter.add(addedAccountsSection)
    }

    private fun selectAccount(account: DebugAccount) {
        getPlugin<AccountsPlugin>().authenticator.authenticate(account)
    }

    private fun render(state: AccountsViewState) {
        preInstalledAccountsSection.update(state.preInstalledItems)
        addedAccountsSection.update(state.addedItems)
    }

}
