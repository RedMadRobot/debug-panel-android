package com.redmadrobot.account_plugin.ui.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.redmadrobot.account_plugin.R
import kotlinx.android.synthetic.main.dialog_add_pin.*

class AddPinDialog : DialogFragment() {

    companion object {
        private const val TAG = "AddPinDialog"

        fun show(targetFragment: Fragment) {
            AddPinDialog()
                .apply { setTargetFragment(targetFragment, 0) }
                .show(targetFragment.parentFragmentManager, TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_pin, container, false)
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
        pin_input.requestFocus()
    }


    private fun setView() {
        done_button.setOnClickListener {
            val pin = pin_input.text?.toString() ?: ""
            (targetFragment as? PinDialogListener)?.onPinAdded(pin)
            dialog?.dismiss()
        }
    }

    interface PinDialogListener {
        fun onPinAdded(pin: String)
    }
}
