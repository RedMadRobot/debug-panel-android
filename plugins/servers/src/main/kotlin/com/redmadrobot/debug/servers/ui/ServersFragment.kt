package com.redmadrobot.debug.servers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.common.extension.obtainShareViewModel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.servers.plugin.ServersPlugin
import com.redmadrobot.debug.servers.plugin.ServersPluginContainer

internal class ServersFragment : Fragment() {

    private val isSettingMode: Boolean by lazy {
        activity?.javaClass?.simpleName == "DebugActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(inflater.context).apply {
        setContent {
            ServersScreen(isEditMode = isSettingMode)
        }
    }
}
