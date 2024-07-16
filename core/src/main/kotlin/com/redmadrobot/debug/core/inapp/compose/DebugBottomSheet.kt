@file:OptIn(ExperimentalFoundationApi::class)

package com.redmadrobot.debug.core.inapp.compose

import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material.MdcTheme
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.panel.core.R
import kotlinx.coroutines.launch
import com.redmadrobot.debug.panel.common.R as CommonR

@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun DebugBottomSheet(onClose: () -> Unit) {
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden) onClose()
            true
        }
    )

    MdcTheme(context = ContextThemeWrapper(LocalContext.current, CommonR.style.DebugPanelTheme)) {
        ModalBottomSheetLayout(
            sheetContent = { BottomSheetContent() },
            sheetState = state,
            content = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BottomSheetContent() {
    val plugins = remember { getAllPlugins() }
    val pluginsName = remember { plugins.map { it.getName() } }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { plugins.size })
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        // Handle
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(50.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        PluginsTabLayout(pluginsName, pagerState = pagerState)
        PluginsPager(plugins, pagerState = pagerState)
    }
}

@Composable
private fun PluginsTabLayout(pluginsName: List<String>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.background,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp,
                color = MaterialTheme.colors.secondary
            )
        }
    ) {
        pluginsName.forEachIndexed { index, _ ->
            Tab(
                text = { Text(pluginsName[index]) },
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
    plugins[pagerState.currentPage].content()
    HorizontalPager(state = pagerState) {
        // no impl. Temporary solution
    }
}
