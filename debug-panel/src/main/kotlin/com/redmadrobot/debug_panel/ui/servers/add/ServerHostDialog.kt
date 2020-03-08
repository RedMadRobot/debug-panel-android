package com.redmadrobot.debug_panel.ui.servers.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.extension.obtainShareViewModel
import com.redmadrobot.debug_panel.internal.DebugPanel
import kotlinx.android.synthetic.main.dialog_server.*

class ServerHostDialog() : DialogFragment() {

    private val shareViewModel by lazy {
        obtainShareViewModel {
            DebugPanel.getContainer().createServersViewModel()
        }
    }

    private var isEditMode = false

    companion object {
        const val HOST = "HOST"
        private const val TAG = "AddServerDialog"

        fun show(fragmentManager: FragmentManager, params: Bundle? = null) {
            ServerHostDialog().apply {
                arguments = params
            }.show(fragmentManager, TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_server, container, false)
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
        obtainArguments()
    }

    private fun obtainArguments() {
        val host = arguments?.getString(HOST)
        host?.let {
            server_host.setText(it)
            isEditMode = true
        }
    }

    private fun setView() {
        save_server_button.setOnClickListener {
            if (isEditMode) {
                update()
            } else {
                save()
            }
        }
        server_host.requestFocus()
    }

    private fun update() {
        val oldValue = arguments?.getString(HOST)
        val newValue = server_host.text.toString()
        oldValue?.let {
            shareViewModel.updateServerHost(oldValue, newValue)
        }
        dialog?.dismiss()
    }

    private fun save() {
        val host = server_host.text.toString()
        shareViewModel.addServer(host)
        dialog?.dismiss()
    }
}
