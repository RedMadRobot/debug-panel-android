package com.redmadrobot.debug.plugin.konfeature.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePlugin
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePluginContainer
import com.redmadrobot.debug.plugin.konfeature.R
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureItem
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureViewState
import com.redmadrobot.debug.uikit.components.PanelSearchBar
import com.redmadrobot.debug.uikit.components.PanelToggle
import com.redmadrobot.debug.uikit.theme.DebugPanelDimensions
import com.redmadrobot.debug.uikit.theme.DebugPanelShapes
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.debug.uikit.theme.MonoFontFamily

@Composable
internal fun KonfeatureScreen(
    viewModel: KonfeatureViewModel = provideViewModel {
        getPlugin<KonfeaturePlugin>()
            .getContainer<KonfeaturePluginContainer>()
            .createKonfeatureViewModel()
    },
) {
    val state by viewModel.state.collectAsState()

    KonfeatureLayout(
        state = state,
        onRefreshClick = viewModel::onRefreshClick,
        onResetAllClick = viewModel::onResetAllClick,
        onCollapseAllClick = viewModel::onCollapseAllClick,
        onHeaderClick = viewModel::onConfigHeaderClick,
        onEditClick = viewModel::onEditClick,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onBooleanToggle = viewModel::onValueChanged,
    )

    state.editDialogState?.let { dialogState ->
        EditConfigValueDialog(
            state = dialogState,
            onValueChange = viewModel::onValueChanged,
            onValueReset = viewModel::onValueReset,
            onDismissRequest = viewModel::onEditDialogCloseClicked,
        )
    }
}

@Composable
internal fun KonfeatureLayout(
    state: KonfeatureViewState,
    onEditClick: (String, Any, Boolean) -> Unit,
    onRefreshClick: () -> Unit,
    onCollapseAllClick: () -> Unit,
    onResetAllClick: () -> Unit,
    onHeaderClick: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onBooleanToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = DebugPanelTheme.colors.background.primary)
    ) {
        ToolbarChips(
            onRefreshClick = onRefreshClick,
            onCollapseAllClick = onCollapseAllClick,
            onResetAllClick = onResetAllClick,
        )
        PanelSearchBar(
            query = state.searchQuery,
            onQueryChange = onSearchQueryChange,
            placeholder = stringResource(R.string.konfeature_plugin_search_hint),
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        AnimatedVisibility(visible = state.shouldShowEmptySearchItemsHint) {
            Text(
                text = stringResource(R.string.konfeature_plugin_search_empty),
                style = DebugPanelTheme.typography.bodyMedium,
                color = DebugPanelTheme.colors.content.tertiary,
                modifier = Modifier.padding(all = 16.dp),
            )
        }
        LazyColumn(modifier = Modifier.weight(weight = 1f)) {
            konfeatureItems(
                state = state,
                onHeaderClick = onHeaderClick,
                onEditClick = onEditClick,
                onBooleanToggle = onBooleanToggle
            )
        }
    }
}

private fun LazyListScope.konfeatureItems(
    state: KonfeatureViewState,
    onHeaderClick: (String) -> Unit,
    onEditClick: (String, Any, Boolean) -> Unit,
    onBooleanToggle: (String, Boolean) -> Unit,
) {
    items(
        items = state.filteredItems,
        key = { item ->
            when (item) {
                is KonfeatureItem.Config -> "config_${item.name}"
                is KonfeatureItem.Value -> "value_${item.key}"
            }
        },
    ) { item ->
        when (item) {
            is KonfeatureItem.Config -> {
                val isCollapsed = !state.isSearchActive && item.name in state.collapsedConfigs
                val overrideCount = state.values.count { value ->
                    value.configName == item.name && value.isDebugSource
                }
                ConfigGroupHeader(
                    name = item.description.takeIf { it.isNotEmpty() } ?: item.name,
                    overrideCount = overrideCount,
                    isCollapsed = isCollapsed,
                    onClick = { onHeaderClick(item.name) },
                )
            }

            is KonfeatureItem.Value -> {
                val isVisible = state.isSearchActive || item.configName !in state.collapsedConfigs
                if (isVisible) {
                    ConfigValueItem(
                        item = item,
                        onEditClick = onEditClick,
                        onBooleanToggle = onBooleanToggle,
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolbarChips(
    onRefreshClick: () -> Unit,
    onCollapseAllClick: () -> Unit,
    onResetAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        ActionChip(
            label = stringResource(R.string.konfeature_plugin_refresh),
            onClick = onRefreshClick,
        )
        ActionChip(
            label = stringResource(R.string.konfeature_plugin_collapse_all),
            onClick = onCollapseAllClick,
        )
        ActionChip(
            label = stringResource(R.string.konfeature_plugin_reset_all),
            onClick = onResetAllClick,
        )
    }
}

@Composable
private fun ActionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        style = DebugPanelTheme.typography.labelLarge,
        color = DebugPanelTheme.colors.content.secondary,
        modifier = modifier
            .clip(shape = DebugPanelShapes.medium)
            .border(
                width = 1.dp,
                color = DebugPanelTheme.colors.stroke.primary,
                shape = DebugPanelShapes.medium,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

@Composable
private fun ConfigGroupHeader(
    name: String,
    overrideCount: Int,
    isCollapsed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = DebugPanelShapes.medium)
            .clickable(onClick = onClick)
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        Icon(
            painter = painterResource(
                id = if (isCollapsed) {
                    R.drawable.icon_keyboard_arrow_up
                } else {
                    R.drawable.icon_keyboard_arrow_down
                }
            ),
            contentDescription = null,
            tint = DebugPanelTheme.colors.content.tertiary,
            modifier = Modifier.size(size = DebugPanelDimensions.iconSizeSmall),
        )
        Text(
            text = name,
            style = DebugPanelTheme.typography.titleMedium,
            color = DebugPanelTheme.colors.content.primary,
            modifier = Modifier.weight(weight = 1f),
        )
        if (overrideCount > 0) {
            Text(
                text = overrideCount.toString(),
                style = DebugPanelTheme.typography.labelSmall,
                color = DebugPanelTheme.colors.content.accent,
                modifier = Modifier
                    .background(
                        color = DebugPanelTheme.colors.surface.tertiary,
                        shape = DebugPanelShapes.small,
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            )
        }
    }
}

@Composable
private fun ConfigValueItem(
    item: KonfeatureItem.Value,
    onEditClick: (String, Any, Boolean) -> Unit,
    onBooleanToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 8.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        ValueInfoColumn(
            item = item,
            modifier = Modifier.weight(weight = 1f),
        )

        when {
            item.value is Boolean -> PanelToggle(
                checked = item.value,
                onCheckedChange = { newValue -> onBooleanToggle(item.key, newValue) },
            )

            item.editAvailable -> EditButton(
                onClick = { onEditClick(item.key, item.value, item.isDebugSource) },
            )
        }
    }
}

@Composable
private fun ValueInfoColumn(
    item: KonfeatureItem.Value,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = item.key,
            style = DebugPanelTheme.typography.bodyMedium.copy(fontFamily = MonoFontFamily),
            color = DebugPanelTheme.colors.content.secondary,
        )
        if (item.description.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = item.description,
                style = DebugPanelTheme.typography.bodyMedium.copy(fontFamily = MonoFontFamily),
                color = DebugPanelTheme.colors.content.tertiary,
            )
        }
        if (item.value is Boolean) {
            ValueSourceLabel(item = item, modifier = Modifier.padding(top = 8.dp))
        } else {
            ValueWithSource(item = item)
        }
    }
}

@Composable
private fun ValueWithSource(
    item: KonfeatureItem.Value,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        Text(
            text = formatValue(value = item.value),
            style = DebugPanelTheme.typography.labelMedium,
            color = sourceColor(item = item),
        )
        ValueSourceLabel(item = item)
    }
}

@Composable
private fun ValueSourceLabel(
    item: KonfeatureItem.Value,
    modifier: Modifier = Modifier,
) {
    when {
        item.isDebugSource -> SourceLabel(
            source = item.sourceName,
            isDebug = true,
            modifier = modifier,
        )

        item.sourceName != "Default" -> SourceLabel(
            source = item.sourceName,
            isDebug = false,
            modifier = modifier,
        )
    }
}

@Composable
private fun EditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(size = DebugPanelDimensions.iconSizeLarge),
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_edit),
            contentDescription = null,
            tint = DebugPanelTheme.colors.content.accent,
            modifier = Modifier.size(size = DebugPanelDimensions.iconSizeSmall),
        )
    }
}

@Composable
private fun SourceLabel(
    source: String,
    isDebug: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = source,
        style = DebugPanelTheme.typography.labelMedium,
        color = if (isDebug) {
            DebugPanelTheme.colors.content.teal
        } else {
            DebugPanelTheme.colors.source.remoteText
        },
        modifier = modifier,
    )
}

private fun formatValue(value: Any): String {
    return if (value is String) "\"$value\"" else value.toString()
}

@Composable
private fun sourceColor(item: KonfeatureItem.Value): Color = when {
    item.isDebugSource -> DebugPanelTheme.colors.content.teal
    item.sourceName != "Default" -> DebugPanelTheme.colors.source.remoteText
    else -> DebugPanelTheme.colors.content.tertiary
}
