@file:OptIn(ExperimentalFoundationApi::class)

package com.redmadrobot.debug.core.inapp.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.R
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.uikit.theme.model.ThemeMode
import kotlinx.coroutines.launch

@Suppress("UnusedParameter")
@Composable
public fun DebugPanelScreen(
    themeMode: ThemeMode,
    onClose: () -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit = {}
) {
    val plugins = remember { getAllPlugins() }
    val pluginsName = remember { plugins.map { it.getName() } }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { plugins.size })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.debug_panel)) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            PluginsTabLayout(pluginsName = pluginsName, pagerState = pagerState)
            PluginsPager(plugins = plugins, pagerState = pagerState)
        }
    }
}

@Composable
private fun PluginsTabLayout(pluginsName: List<String>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.background,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(
                    currentTabPosition = tabPositions[pagerState.currentPage]
                ),
                height = 2.dp,
                color = MaterialTheme.colors.secondary
            )
        }
    ) {
        pluginsName.forEachIndexed { index, _ ->
            Tab(
                text = { Text(text = pluginsName[index]) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    }
}

@Composable
private fun PluginsPager(plugins: List<Plugin>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        plugins[page].content()
    }
}
