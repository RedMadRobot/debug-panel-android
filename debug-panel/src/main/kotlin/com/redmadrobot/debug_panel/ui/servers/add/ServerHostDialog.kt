package com.redmadrobot.debug_panel.ui.servers.add

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.core.extension.getPlugin
import com.redmadrobot.core.extension.obtainShareViewModel
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.internal.plugin.server.ServersPlugin
import com.redmadrobot.debug_panel.internal.plugin.server.ServersPluginContainer
import kotlinx.android.synthetic.main.dialog_server.*

class ServerHostDialog : DialogFragment() {

    companion object {
        const val HOST = "HOST"
        private const val TAG = "AddServerDialog"

        fun show(fragmentManager: FragmentManager, params: Bundle? = null) {
            ServerHostDialog().apply {
                arguments = params
            }.show(fragmentManager, TAG)
        }
    }

    private val shareViewModel by lazy {
        obtainShareViewModel {
            getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .createServersViewModel()
        }
    }

    private var isEditMode = false

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
            val host = server_host.text.toString()
            if (isHostValid(host)) {
                save(host)
            } else {
                showWrongHostError()
            }
        }
        server_host.requestFocus()
    }

    private fun save(host: String) {
        if (isEditMode) {
            update(host)
        } else {
            saveNew(host)
        }
    }

    private fun update(newHost: String) {
        val oldValue = arguments?.getString(HOST)
        oldValue?.let {
            shareViewModel.updateServerData(oldValue, newHost)
        }
        dialog?.dismiss()
    }

    private fun saveNew(host: String) {
        shareViewModel.addServer(host)
        dialog?.dismiss()
    }

    private fun isHostValid(host: String): Boolean {
        return Patterns.WEB_URL.matcher(host).matches()
    }

    private fun showWrongHostError() {
        server_host_input_layout.error = getString(R.string.error_wrong_host)
    }
}
