package com.redmadrobot.account_plugin.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.account_plugin.R
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.account_plugin.plugin.AccountsPluginContainer
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.extension.obtainShareViewModel
import kotlinx.android.synthetic.main.dialog_add_account.*

class AddAccountDialog : DialogFragment() {

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
        return inflater.inflate(R.layout.dialog_add_account, container, false)
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
        setView()
        setData()
        account_login.requestFocus()
    }

    private fun setData() {
        if (!login.isNullOrEmpty() && password != null) {
            account_login.setText(login)
            account_password.setText(password)
            account_pin.setText(pin)
        }
    }

    private fun setView() {
        save_account_button.setOnClickListener {
            if (dataIsValid()) save()
        }
    }

    private fun save() {
        val login = account_login.text.toString()
        val password = account_password.text.toString()
        val pin = account_pin.text.toString()
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
