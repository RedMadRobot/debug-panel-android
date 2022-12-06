package com.redmadrobot.debug.account.ui

import android.os.Bundle
import android.view.View
import com.redmadrobot.debug.account.plugin.AccountsPlugin
import com.redmadrobot.debug.account.plugin.AccountsPluginContainer
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.databinding.FragmentAccountsBinding
import com.redmadrobot.account_plugin.databinding.ItemAccountBinding
import com.redmadrobot.account_plugin.ui.add.AddAccountDialog
import com.redmadrobot.account_plugin.ui.item.DebugAccountItems
import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug.account.plugin.AccountSelectedEvent
import com.redmadrobot.debug_panel_common.databinding.ItemSectionHeaderBinding
import com.redmadrobot.debug.common.base.PluginFragment
import com.redmadrobot.debug.common.extension.observe
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.itemsadapter.ItemsAdapter
import com.redmadrobot.itemsadapter.bind
import com.redmadrobot.itemsadapter.itemsAdapter
import com.redmadrobot.debug_panel_common.R as CommonR

internal class AccountsFragment : PluginFragment(R.layout.fragment_accounts) {

    companion object {
        const val IS_EDIT_MODE_KEY = "IS_EDIT_MODE_KEY"
    }

    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val isEditMode by lazy {
        requireNotNull(arguments).getBoolean(IS_EDIT_MODE_KEY)
    }

    private val viewModel by lazy {
        obtainShareViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(viewModel.state, ::render)
        viewModel.loadAccounts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountsBinding.bind(view)
        binding.setView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentAccountsBinding.setView() {
        accountList.layoutManager = LinearLayoutManager(requireContext())
        addAccount.setOnClickListener {
            AddAccountDialog.show(
                requireActivity().supportFragmentManager
            )
        }
        addAccount.isVisible = isEditMode
    }

    private fun render(state: AccountsViewState) {
        val adapter = createAdapterByState(state)
        binding.accountList.adapter = adapter
    }

    private fun createAdapterByState(state: AccountsViewState): ItemsAdapter {
        return itemsAdapter(state.preInstalledAccounts.plus(state.addedAccounts)) { item ->
            when (item) {
                is DebugAccountItems.Header -> {
                    bind<ItemSectionHeaderBinding>(CommonR.layout.item_section_header) {
                        itemSectionTitle.text = item.header
                    }
                }
                is DebugAccountItems.PreinstalledAccount -> {
                    bind<ItemAccountBinding>(R.layout.item_account) {
                        accountLogin.text = item.account.login
                        if (!isEditMode) {
                            root.setOnClickListener { setAccountAsCurrent(item.account) }
                        }
                    }
                }
                is DebugAccountItems.AddedAccount -> {
                    bind<ItemAccountBinding>(R.layout.item_account) {
                        accountLogin.text = item.account.login
                        accountDelete.isVisible = isEditMode
                        accountDelete.setOnClickListener { viewModel.removeAccount(item.account) }
                        root.setOnClickListener { onAddedAccountClicked(item.account) }
                    }
                }
            }
        }
    }

    private fun onAddedAccountClicked(account: DebugAccount) {
        if (isEditMode) openAccountDialog(account) else setAccountAsCurrent(account)
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
