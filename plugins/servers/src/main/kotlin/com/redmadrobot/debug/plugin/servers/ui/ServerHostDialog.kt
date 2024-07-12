package com.redmadrobot.debug.plugin.servers.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.databinding.DialogServerBinding
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.ServersPluginContainer

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

    private var _binding: DialogServerBinding? = null
    private val binding get() = checkNotNull(_binding)

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
        _binding = DialogServerBinding.inflate(inflater, container, false)
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
        binding.setViews()
        obtainArguments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainArguments() {
        val name = arguments?.getString(KEY_NAME)
        val url = arguments?.getString(KEY_URL)
        if (!name.isNullOrEmpty() && !url.isNullOrEmpty()) {
            binding.serverName.setText(name)
            binding.serverHost.setText(url)
            isEditMode = true
        }
    }

    private fun DialogServerBinding.setViews() {
        saveServerButton.setOnClickListener {
            val name = serverName.text.toString()
            val url = serverHost.text.toString()
            if (isDataValid(name, url)) {
                save(name, url)
            } else {
                showWrongHostError()
            }
        }
        serverName.requestFocus()
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
        binding.serverNameInputLayout.error = getString(R.string.error_empty_name)
    }

    private fun showWrongHostError() {
        binding.serverNameInputLayout.error = getString(R.string.error_wrong_host)
    }
}
