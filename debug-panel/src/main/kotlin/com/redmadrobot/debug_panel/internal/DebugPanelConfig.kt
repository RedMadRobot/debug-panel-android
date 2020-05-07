package com.redmadrobot.debug_panel.internal

import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.accounts.DefaultAuthenticator
import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig

data class DebugPanelConfig(
    val authenticator: Authenticator = DefaultAuthenticator(),
    val preInstalledServers: PreInstalledData<DebugServer> = PreInstalledData(emptyList()),
    val preInstalledAccounts: PreInstalledData<DebugAccount> = PreInstalledData(emptyList()),
    val featureTogglesConfig: FeatureTogglesConfig? = null
)
