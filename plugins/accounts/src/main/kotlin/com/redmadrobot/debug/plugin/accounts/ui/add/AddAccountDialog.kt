package com.redmadrobot.debug.plugin.accounts.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
import com.redmadrobot.debug.plugin.accounts.databinding.DialogAddAccountBinding
import com.redmadrobot.debug.plugin.accounts.AccountsPlugin
import com.redmadrobot.debug.plugin.accounts.AccountsPluginContainer
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin

internal class AddAccountDialog : DialogFragment() {

    companion object {
        private const val KEY_ID = "ID"
        private const val KEY_LOGIN = "LOGIN"
        private const val KEY_PASSWORD = "PASSWORD"
        private const val KEY_PIN = "PIN"

        private const val TAG = "AddAccountDialog"

        fun show(
            fragmentManager: FragmentManager,
            account: DebugAccount? = null
        ) {
            AddAccountDialog().apply {
                if (account != null) {
                    this.arguments = bundleOf(
                        KEY_ID to account.id,
                        KEY_LOGIN to account.login,
                        KEY_PASSWORD to account.password,
                        KEY_PIN to account.pin
                    )
                }
            }.show(fragmentManager, TAG)
        }
    }

    private var _binding: DialogAddAccountBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val id by lazy { arguments?.getInt(KEY_ID) }
    private val login by lazy { arguments?.getString(KEY_LOGIN) }
    private val password by lazy { arguments?.getString(KEY_PASSWORD) }
    private val pin by lazy { arguments?.getString(KEY_PIN) }

    private val isEditMode: Boolean
        get() = login != null && password != null

    private val sharedViewModel by lazy {
        obtainShareViewModel {
            getPlugin<AccountsPlugin>()
                .getContainer<AccountsPluginContainer>()
                .createAccountsViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAddAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setView()
        binding.setData()
        binding.accountLogin.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun DialogAddAccountBinding.setData() {
        if (!login.isNullOrEmpty() && password != null) {
            accountLogin.setText(login)
            accountPassword.setText(password)
            accountPin.setText(pin)
        }
    }

    private fun DialogAddAccountBinding.setView() {
        saveAccountButton.setOnClickListener {
            if (dataIsValid()) save()
        }
    }

    private fun save() {
        val login = binding.accountLogin.text.toString()
        val password = binding.accountPassword.text.toString()
        val pin = binding.accountPin.text.toString()
        if (isEditMode) {
            id?.let { id ->
                sharedViewModel.updateAccount(id, login, password, pin)
            }
        } else {
            sharedViewModel.saveAccount(login, password, pin)
        }
        dialog?.dismiss()
    }


    private fun dataIsValid(): Boolean {
        //TODO Добавить валидацию данных
        return true
    }
}
