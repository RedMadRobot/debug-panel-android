package com.redmadrobot.debug_panel.ui.accounts.add

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.extension.observe
import com.redmadrobot.debug_panel.extension.obtainViewModel
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.ui.base.BaseFragment
import com.redmadrobot.debug_panel.ui.view.ItemTouchHelperCallback
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_add_account.*

class AccountsFragment : BaseFragment(R.layout.fragment_add_account){

    companion object {
        fun getInstance() = AccountsFragment()
    }

    private val accountsViewModel by lazy {
        obtainViewModel {
            DebugPanel.getContainer().createAccountsViewModel()
        }
    }

    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(accountsViewModel.state, ::setAccountList)
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

        val itemTouchHelperCallback = ItemTouchHelperCallback({ position ->
            accountsViewModel.removeAccount(position)
        })

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(account_list)
        }

        add_account.setOnClickListener {
            AddAccountDialog.show(requireActivity().supportFragmentManager)
        }
    }

    private fun setAccountList(accounts: List<Item>) {
        accountsAdapter.update(accounts)
    }
}
