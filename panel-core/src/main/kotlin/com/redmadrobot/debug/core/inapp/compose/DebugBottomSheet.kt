@file:OptIn(ExperimentalFoundationApi::class)

package com.redmadrobot.debug.core.inapp.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.debug.core.plugin.Plugin
import kotlinx.coroutines.launch

@Composable
public fun DebugBottomSheet(onClose: () -> Unit) {
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmValueChange = { sheetState ->
            if (sheetState == ModalBottomSheetValue.Hidden) onClose()
            true
        },
        skipHalfExpanded = false
    )

    ModalBottomSheetLayout(
        sheetContent = { BottomSheetContent() },
        sheetState = state,
        scrimColor = Color.Transparent,
        content = {},
    )
}

@Composable
private fun BottomSheetContent() {
    val plugins = remember { getAllPlugins() }
    val pluginsName = remember { plugins.map { it.getName() } }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { plugins.size })
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))

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
    HorizontalPager(state = pagerState) { page ->
        plugins[page].content()
    }
}
