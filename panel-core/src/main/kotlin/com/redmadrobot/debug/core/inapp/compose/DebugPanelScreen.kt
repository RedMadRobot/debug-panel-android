@file:OptIn(ExperimentalFoundationApi::class)

package com.redmadrobot.debug.core.inapp.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.R
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.debug.uikit.components.ThemeSwitcher
import com.redmadrobot.debug.uikit.theme.DebugPanelDimensions
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.debug.uikit.theme.model.ThemeMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
public fun DebugPanelScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onClose: () -> Unit,
) {
    val plugins = remember { getAllPlugins() }
    val pluginNames = remember { plugins.map { it.getName() } }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { plugins.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DebugPanelTheme.colors.background.primary),
    ) {
        PanelTopBar(
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange,
            onClose = onClose,
        )
        PanelTabRow(
            pluginNames = pluginNames.toImmutableList(),
            selectedIndex = pagerState.currentPage,
            onTabClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
        )
        HorizontalPager(state = pagerState, modifier = Modifier.weight(weight = 1f)) { page ->
            plugins[page].content()
        }
    }
}

@Composable
private fun PanelTopBar(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = DebugPanelDimensions.topBarHeight)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.debug_panel),
            style = DebugPanelTheme.typography.titleLarge,
            color = DebugPanelTheme.colors.content.primary,
            modifier = Modifier
                .weight(weight = 1f)
                .padding(start = 4.dp),
        )
        ThemeSwitcher(
            currentMode = themeMode,
            onModeSelect = onThemeModeChange,
        )
        IconButton(onClick = onClose) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = DebugPanelTheme.colors.content.secondary,
                modifier = Modifier.size(size = DebugPanelDimensions.iconSizeMedium),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PanelTabRow(
    pluginNames: ImmutableList<String>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = DebugPanelDimensions.tabRowHeight)
            .background(color = DebugPanelTheme.colors.surface.secondary),
        edgePadding = 4.dp,
        containerColor = DebugPanelTheme.colors.surface.secondary,
        contentColor = DebugPanelTheme.colors.content.secondary,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    selectedTabIndex = selectedIndex,
                    matchContentSize = false,
                ),
                height = 2.dp,
                color = DebugPanelTheme.colors.content.accent,
            )
        },
        divider = {},
    ) {
        pluginNames.forEachIndexed { index, title ->
            key(title) {
                PluginTab(
                    isSelected = selectedIndex == index,
                    title = title,
                    onTabClick = { onTabClick(index) }
                )
            }
        }
    }
}

@Composable
private fun PluginTab(
    title: String,
    isSelected: Boolean,
    onTabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Tab(
        modifier = modifier.padding(horizontal = 4.dp),
        selected = isSelected,
        onClick = { onTabClick.invoke() },
        text = { PluginTabTitle(title = title, isSelected = isSelected) },
    )
}

@Composable
private fun PluginTabTitle(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val titleColor = if (isSelected) {
        DebugPanelTheme.colors.content.accent
    } else {
        DebugPanelTheme.colors.content.secondary
    }

    Text(
        modifier = modifier,
        text = title,
        style = DebugPanelTheme.typography.labelLarge,
        color = titleColor,
    )
}
