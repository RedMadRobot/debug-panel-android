package com.redmadrobot.account_plugin.ui.select

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.plugin.AccountSelectedEvent
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.account_plugin.plugin.AccountsPluginContainer
import com.redmadrobot.account_plugin.ui.AccountsViewState
import com.redmadrobot.account_plugin.ui.item.AccountItem
import com.redmadrobot.debug_panel_common.base.PluginFragment
import com.redmadrobot.debug_panel_common.extension.observe
import com.redmadrobot.debug_panel_common.extension.obtainViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_account_select.*

internal class AccountSelectionFragment : PluginFragment(R.layout.fragment_account_select) {

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
            account?.let { onAccountSelected(it) }
        }
        accountsAdapter.add(preInstalledAccountsSection)
        accountsAdapter.add(addedAccountsSection)
    }

    private fun onAccountSelected(account: DebugAccount) {
        getPlugin<AccountsPlugin>().debugAuthenticator.onAccountSelected(account)
        pushEvent(account)
    }

    private fun showError(localizedMessage: String?) {
        Toast.makeText(
            requireActivity(),
            localizedMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun pushEvent(account: DebugAccount) {
        Toast.makeText(
            requireActivity(),
            "Account ${account.login} selected",
            Toast.LENGTH_SHORT
        ).show()
        getPlugin<AccountsPlugin>().pushEvent(AccountSelectedEvent(account))
    }

    private fun render(state: AccountsViewState) {
        preInstalledAccountsSection.update(state.preInstalledItems)
        addedAccountsSection.update(state.addedItems)
    }

}
