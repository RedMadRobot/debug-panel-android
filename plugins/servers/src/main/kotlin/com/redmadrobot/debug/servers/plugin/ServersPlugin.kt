package com.redmadrobot.debug.servers.plugin

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugServerData
import com.redmadrobot.debug.servers.data.model.DebugStage
import com.redmadrobot.debug.servers.ui.ServersFragment
import com.redmadrobot.debug.servers.ui.ServersScreen
import com.redmadrobot.debug.servers.databinding.FragmentContainerServersBinding
import kotlinx.coroutines.runBlocking

public class ServersPlugin(
    private val preInstalledServers: List<DebugServerData> = emptyList(),
) : Plugin() {

    init {
        preInstalledServers.find { it.isDefault }
            ?: error("ServersPlugin can't be initialized. At least one server must be default")
    }

    public companion object {
        internal const val NAME = "SERVERS"

        public fun getSelectedServer(): DebugServer {
            return runBlocking {
                getPlugin<ServersPlugin>()
                    .getContainer<ServersPluginContainer>()
                    .serversRepository
                    .getSelectedServer()
            }
        }

        public fun getDefaultServer(): DebugServer {
            return getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .serversRepository
                .getDefault()
        }

        public fun getSelectedStage(): DebugStage {
            return getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .stagesRepository
                .getSelectedStage()
        }

        public fun getDefaultStage(): DebugStage {
            return getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .stagesRepository
                .getDefault()
        }
    }

    public constructor(preInstalledServers: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = preInstalledServers.provideData()
    )

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return ServersPluginContainer(preInstalledServers, commonContainer)
    }

    @Deprecated(
        "You shouldn't use fragments for you plugins. Please use Jetpack Compose",
        replaceWith = ReplaceWith("content()")
    )
    override fun getFragment(): Fragment {
        return ServersFragment()
    }

    @Deprecated(
        "You shouldn't use fragments for you plugins. Please use Jetpack Compose",
        replaceWith = ReplaceWith("settingsContent()")
    )
    override fun getSettingFragment(): Fragment {
        return ServersFragment()
    }

    @Composable
    override fun content() {
        ServersScreen(isEditMode = false)
    }

    @Composable
    override fun settingsContent() {
        AndroidViewBinding(FragmentContainerServersBinding::inflate)
    }
}
