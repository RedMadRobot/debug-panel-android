package com.redmadrobot.debug_panel.accounts.ui.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.accounts.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.PreinstalledAccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.ui.item.UserCredentialsItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.bottom_sheet_account_select.*

class AccountSelectBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "AccountSelectBottomSheet"
        const val REQUEST_CODE_ACCOUNT = 123123

        fun <T> show(
            fragmentManager: FragmentManager,
            resultListener: T
        ) where T : Fragment, T : AccountDataResultListener {

            AccountSelectBottomSheet()
                .apply {
                    setTargetFragment(resultListener, REQUEST_CODE_ACCOUNT)
                }
                .show(fragmentManager, TAG)
        }

        fun show(fragmentManager: FragmentManager) {
            AccountSelectBottomSheet()
                .show(fragmentManager, TAG)
        }
    }

    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_account_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        loadMockData()
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
        dismiss()
    }

    private fun loadMockData() {
        val credentialsProvider = AccountsProvider(PreinstalledAccountsLoadStrategy())
        val accountItems = credentialsProvider.getAccounts()
            .map(::UserCredentialsItem)

        accountsAdapter.update(accountItems)
    }

    interface AccountDataResultListener {
        fun onAccountSelected(login: String, password: String)
    }
}
