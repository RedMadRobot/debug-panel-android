package com.redmadrobot.debug_panel.internal

import android.content.Context
import androidx.room.RoomDatabase
import com.redmadrobot.debug_panel.data.accounts.DebugAccountRepository
import com.redmadrobot.debug_panel.data.accounts.LocalDebugAccountRepository
import com.redmadrobot.debug_panel.data.servers.DebugServerRepository
import com.redmadrobot.debug_panel.data.servers.LocalDebugServerRepository
import com.redmadrobot.debug_panel.data.settings.AppSettingsRepository
import com.redmadrobot.debug_panel.data.settings.AppSettingsRepositoryImpl
import com.redmadrobot.debug_panel.data.storage.AppDatabase
import com.redmadrobot.debug_panel.data.storage.PanelSettingsRepository
import com.redmadrobot.debug_panel.data.toggles.LocalFeatureToggleRepository
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleHolder
import com.redmadrobot.debug_panel.ui.accounts.AccountsViewModel
import com.redmadrobot.debug_panel.ui.servers.ServersViewModel
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesViewModel

class DebugPanelContainer(
    private val context: Context,
    debugPanelConfig: DebugPanelConfig
) {

    private val dataBaseInstance: RoomDatabase

    /*Accounts region*/
    private val debugAccountRepository: DebugAccountRepository
    /*endregion*/

    /*Servers region*/
    private val serversRepository: DebugServerRepository
    /*endregion*/

    /*Feature toggle region*/
    internal val panelSettingsRepository: PanelSettingsRepository
    private val localFeatureToggleRepository: LocalFeatureToggleRepository
    internal val featureToggleHolder: FeatureToggleHolder
    /*endregion*/

    /*App settings*/
    internal val appSettingsRepository: AppSettingsRepository
    /*endregion*/

    init {
        this.dataBaseInstance = AppDatabase.getInstance(context)

        //Accounts
        this.debugAccountRepository = LocalDebugAccountRepository(
            dataBaseInstance.getDebugAccountsDao(),
            debugPanelConfig.preInstalledAccounts
        )
        //

        //Servers
        this.serversRepository = LocalDebugServerRepository(
            dataBaseInstance.getDebugServersDao(),
            debugPanelConfig.preInstalledServers
        )
        //

        //Feature toggle
        this.panelSettingsRepository = PanelSettingsRepository(context)
        this.localFeatureToggleRepository = LocalFeatureToggleRepository(
            dataBaseInstance.getFeatureTogglesDao(),
            PanelSettingsRepository(context)
        )
        this.featureToggleHolder = FeatureToggleHolder(this.localFeatureToggleRepository)
        //

        //Settings
        appSettingsRepository = AppSettingsRepositoryImpl(debugPanelConfig.sharedPreferences)
        //
    }

    fun createAccountsViewModel(): AccountsViewModel {
        return AccountsViewModel(context, debugAccountRepository)
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(context, serversRepository, panelSettingsRepository)
    }

    fun createFeatureTogglesViewModel(): FeatureTogglesViewModel {
        return FeatureTogglesViewModel(localFeatureToggleRepository, panelSettingsRepository)
    }
}
