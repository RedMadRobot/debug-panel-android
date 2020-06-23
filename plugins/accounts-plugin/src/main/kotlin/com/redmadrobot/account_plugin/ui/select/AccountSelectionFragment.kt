package com.redmadrobot.account_plugin.ui.select

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.account_plugin.plugin.AccountsPluginContainer
import com.redmadrobot.account_plugin.ui.AccountsViewState
import com.redmadrobot.account_plugin.ui.item.AccountItem
import com.redmadrobot.account_plugin.ui.pin.AddPinDialog
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.core.extension.getPlugin
import com.redmadrobot.core.extension.observe
import com.redmadrobot.core.extension.obtainViewModel
import com.redmadrobot.core.ui.base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_account_select.*

class AccountSelectionFragment : BaseFragment(R.layout.fragment_account_select),
    AddPinDialog.PinDialogListener {

    private val accountsViewModel by lazy {
        obtainViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }
    private var selectedAccount: DebugAccount? = null
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

    override fun onPinAdded(pin: String) {
        selectedAccount?.let { account ->
            onAccountSelected(account, pin)
        }
    }

    private fun setView() {
        account_select_recycler.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        accountsAdapter.setOnItemClickListener { item, _ ->
            val account = (item as? AccountItem)?.account
            if (account?.pinNeeded == false) {
                onAccountSelected(account)
            } else {
                showAddPinDialog(account)
            }
        }
        accountsAdapter.add(preInstalledAccountsSection)
        accountsAdapter.add(addedAccountsSection)
    }

    private fun showAddPinDialog(account: DebugAccount?) {
        this.selectedAccount = account
        AddPinDialog.show(this)
    }

    private fun onAccountSelected(account: DebugAccount, pin: String? = null) {
        getPlugin<AccountsPlugin>().authenticator.authenticate(account, pin)
        showSelectionInfo(account)
    }

    private fun showSelectionInfo(account: DebugAccount) {
        Toast.makeText(
            requireActivity(),
            "Account ${account.login} selected",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun render(state: AccountsViewState) {
        preInstalledAccountsSection.update(state.preInstalledItems)
        addedAccountsSection.update(state.addedItems)
    }

}
