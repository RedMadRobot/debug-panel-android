package com.redmadrobot.debug.plugin.servers

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServerData
import com.redmadrobot.debug.plugin.servers.databinding.FragmentContainerServersBinding
import com.redmadrobot.debug.plugin.servers.ui.ServersFragment
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
    }

    public constructor(preInstalledServers: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = preInstalledServers.provideData()
    )

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return ServersPluginContainer(preInstalledServers, commonContainer)
    }

    @Deprecated(
        "You should't use fragments for you plugins. Please use Jetpack Compose",
        replaceWith = ReplaceWith("content()", "com.redmadrobot.debug.core.plugin.Plugin")
    )
    override fun getFragment(): Fragment {
        return ServersFragment()
    }

    @Deprecated(
        "You should't use fragments for you plugins. Please use Jetpack Compose",
        replaceWith = ReplaceWith("content()", "com.redmadrobot.debug.core.plugin.Plugin")
    )
    override fun getSettingFragment(): Fragment {
        return ServersFragment()
    }

    @Composable
    override fun content() {
        AndroidViewBinding(
            FragmentContainerServersBinding::inflate,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }

    @Composable
    override fun settingsContent() {
        AndroidViewBinding(
            FragmentContainerServersBinding::inflate,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}
