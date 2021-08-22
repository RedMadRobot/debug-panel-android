package com.redmadrobot.account_plugin.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.databinding.ItemAccountBinding
import com.redmadrobot.account_plugin.plugin.AccountSelectedEvent
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.account_plugin.plugin.AccountsPluginContainer
import com.redmadrobot.account_plugin.ui.add.AddAccountDialog
import com.redmadrobot.account_plugin.ui.item.DebugAccountItems
import com.redmadrobot.debug_panel_common.base.PluginFragment
import com.redmadrobot.debug_panel_common.databinding.ItemSectionHeaderBinding
import com.redmadrobot.debug_panel_common.extension.observe
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.itemsadapter.ItemsAdapter
import com.redmadrobot.itemsadapter.bind
import com.redmadrobot.itemsadapter.itemsAdapter
import kotlinx.android.synthetic.main.fragment_accounts.*

internal class AccountsFragment : PluginFragment(R.layout.fragment_accounts) {

    companion object {
        const val IS_EDIT_MODE_KEY = "IS_EDIT_MODE_KEY"
    }

    private val isEditMode by lazy {
        requireNotNull(arguments).getBoolean(IS_EDIT_MODE_KEY)
    }

    private val accountsViewModel by lazy {
        obtainShareViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }

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
        account_list.layoutManager = LinearLayoutManager(requireContext())
        add_account.setOnClickListener {
            AddAccountDialog.show(
                requireActivity().supportFragmentManager
            )
        }
    }

    private fun render(state: AccountsViewState) {
        val adapter = createAdapterByState(state)
        account_list.adapter = adapter
    }

    private fun createAdapterByState(state: AccountsViewState): ItemsAdapter {
        return itemsAdapter(state.preInstalledAccounts.plus(state.addedAccounts)) { item ->
            when (item) {
                is DebugAccountItems.Header -> {
                    bind<ItemSectionHeaderBinding>(R.layout.item_section_header) {
                        itemSectionTitle.text = item.header
                    }
                }
                is DebugAccountItems.PreinstalledAccount -> {
                    bind<ItemAccountBinding>(R.layout.item_account) {
                        accountLogin.text = item.account.login
                        root.setOnClickListener { setAccountAsCurrent(item.account) }
                    }
                }
                is DebugAccountItems.AddedAccount -> {
                    bind<ItemAccountBinding>(R.layout.item_account) {
                        accountLogin.text = item.account.login
                        root.setOnClickListener { onAddedAccountClicked(item.account) }
                    }
                }
            }
        }
    }

    private fun onAddedAccountClicked(account: DebugAccount) {
        if (isEditMode) {
            openAccountDialog(account)
        } else {
            setAccountAsCurrent(account)
        }
    }

    private fun openAccountDialog(account: DebugAccount) {
        AddAccountDialog.show(
            fragmentManager = requireActivity().supportFragmentManager,
            account = account
        )
    }

    private fun setAccountAsCurrent(account: DebugAccount) {
        getPlugin<AccountsPlugin>().debugAuthenticator.onAccountSelected(account)
        pushEvent(account)
    }

    private fun pushEvent(account: DebugAccount) {
        Toast.makeText(
            requireActivity(),
            "Account ${account.login} selected",
            Toast.LENGTH_SHORT
        ).show()
        getPlugin<AccountsPlugin>().pushEvent(AccountSelectedEvent(account))
    }
}
