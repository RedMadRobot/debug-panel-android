package com.redmadrobot.debug.plugin.aboutapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.aboutapp.AboutAppPlugin
import com.redmadrobot.debug.plugin.aboutapp.AboutAppPluginContainer
import com.redmadrobot.debug.plugin.aboutapp.R
import com.redmadrobot.debug.uikit.theme.DebugPanelShapes
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme

@Composable
internal fun AboutAppScreen(
    viewModel: AboutAppViewModel = provideViewModel {
        getPlugin<AboutAppPlugin>()
            .getContainer<AboutAppPluginContainer>()
            .createAboutAppViewModel()
    },
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            val message = event.message.format(event.item.title)
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        containerColor = DebugPanelTheme.colors.background.primary,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.systemBarsPadding(),
                hostState = snackbarHostState
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(items = state.appInfoList, key = { it.id }) { item ->
                InfoRow(
                    label = item.title,
                    value = item.value,
                    onClick = { message ->
                        viewModel.onInfoItemClicked(message = message, item = item)
                    },
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val copiedMessage = stringResource(id = R.string.about_app_copied)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = DebugPanelShapes.medium)
            .clickable(onClick = { onClick.invoke(copiedMessage) })
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label.uppercase(),
            style = DebugPanelTheme.typography.labelMedium,
            color = DebugPanelTheme.colors.content.tertiary,
            modifier = Modifier.padding(end = 12.dp),
        )
        Text(
            text = value,
            style = DebugPanelTheme.typography.bodySmall,
            color = DebugPanelTheme.colors.content.primary,
            modifier = Modifier.weight(weight = 1f),
            overflow = TextOverflow.Ellipsis,
        )
    }
}
