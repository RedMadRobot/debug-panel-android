package com.redmadrobot.account_plugin.ui.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.account_plugin.plugin.AccountsPluginContainer
import com.redmadrobot.account_plugin.ui.AccountsViewState
import com.redmadrobot.account_plugin.ui.item.AccountItem
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.extension.observe
import com.redmadrobot.debug_panel_core.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.ui.ItemTouchHelperCallback
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_add_account.*

internal class AccountsFragment : Fragment(R.layout.fragment_add_account) {

    private val accountsViewModel by lazy {
        obtainShareViewModel {
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
        account_list.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        setItemTouchHelper()
        with(accountsAdapter) {
            setOnItemClickListener { item, _ ->
                (item as? AccountItem)?.let { handleItemClick(it) }
            }
            add(preInstalledAccountsSection)
            add(addedAccountsSection)
        }

        add_account.setOnClickListener {
            AddAccountDialog.show(
                requireActivity().supportFragmentManager
            )
        }
    }

    private fun setItemTouchHelper() {
        val itemTouchHelperCallback = ItemTouchHelperCallback(
            onSwiped = { position ->
                /*remove account from DB*/
                val item = accountsAdapter.getItem(position) as? AccountItem
                item?.let { accountsViewModel.removeAccount(it.account) }
            },
            canBeSwiped = { position ->
                accountsAdapter.getGroupAtAdapterPosition(position) == addedAccountsSection &&
                        accountsAdapter.getItem(position) is AccountItem
            }
        )
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(account_list)
        }
    }

    private fun handleItemClick(item: AccountItem) {
        if (addedAccountsSection.getPosition(item) >= 0) {
            val account = item.account
            openAddAccountDialog(account)
        }
    }

    private fun openAddAccountDialog(account: DebugAccount) {
        AddAccountDialog.show(
            fragmentManager = requireActivity().supportFragmentManager,
            account = account
        )
    }

    private fun render(state: AccountsViewState) {
        preInstalledAccountsSection.update(state.preInstalledItems)
        addedAccountsSection.update(state.addedItems)
    }
}
