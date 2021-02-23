package com.redmadrobot.servers_plugin.ui.add

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug_panel_common.extension.obtainShareViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.plugin.ServersPlugin
import com.redmadrobot.servers_plugin.plugin.ServersPluginContainer
import kotlinx.android.synthetic.main.dialog_server.*

internal class ServerHostDialog : DialogFragment() {

    companion object {
        const val KEY_NAME = "NAME"
        const val KEY_URL = "URL"
        const val KEY_ID = "ID"
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
        val name = arguments?.getString(KEY_NAME)
        val url = arguments?.getString(KEY_URL)
        if (!name.isNullOrEmpty() && !url.isNullOrEmpty()) {
            server_name.setText(name)
            server_host.setText(url)
            isEditMode = true
        }
    }

    private fun setView() {
        save_server_button.setOnClickListener {
            val name = server_name.text.toString()
            val url = server_host.text.toString()
            if (isDataValid(name, url)) {
                save(name, url)
            } else {
                showWrongHostError()
            }
        }
        server_name.requestFocus()
    }

    private fun save(name: String, url: String) {
        if (isEditMode) {
            update(name, url)
        } else {
            saveNew(name, url)
        }
    }

    private fun update(name: String, url: String) {
        val id = arguments?.getInt(KEY_ID)
        id?.let {
            shareViewModel.updateServerData(id, name, url)
        }
        dialog?.dismiss()
    }

    private fun saveNew(name: String, url: String) {
        shareViewModel.addServer(name, url)
        dialog?.dismiss()
    }

    private fun isDataValid(name: String, url: String): Boolean {
        return when {
            name.isEmpty() -> {
                showEmptyNameError()
                false
            }
            !Patterns.WEB_URL.matcher(url).matches() -> {
                showWrongHostError()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun showEmptyNameError() {
        server_name_input_layout.error = getString(R.string.error_empty_name)
    }

    private fun showWrongHostError() {
        server_host_input_layout.error = getString(R.string.error_wrong_host)
    }
}
