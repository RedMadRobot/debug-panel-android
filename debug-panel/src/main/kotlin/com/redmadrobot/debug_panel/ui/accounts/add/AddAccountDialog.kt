package com.redmadrobot.debug_panel.ui.accounts.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug_panel.R
import kotlinx.android.synthetic.main.dialog_add_account.*

class AddAccountDialog : DialogFragment() {

    companion object {
        private const val TAG = "AddAccountDialog"
        private const val REQUEST_CODE = 112233

        fun show(fragmentManager: FragmentManager, targetFragment: Fragment) {
            AddAccountDialog().apply {
                setTargetFragment(targetFragment, REQUEST_CODE)
            }.show(fragmentManager, TAG)
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
        account_login.requestFocus()
    }

    private fun setView() {
        save_account_button.setOnClickListener { saveAccount() }
    }

    private fun saveAccount() {
        val login = account_login.text.toString()
        val password = account_password.text.toString()
        (targetFragment as? SaveAccountResultListener)?.onAccountSaved(login, password)
        dialog?.dismiss()
    }

    interface SaveAccountResultListener {
        fun onAccountSaved(login: String, password: String)
    }
}
