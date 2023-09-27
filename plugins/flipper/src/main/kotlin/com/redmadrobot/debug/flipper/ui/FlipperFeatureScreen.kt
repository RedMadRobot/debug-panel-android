package com.redmadrobot.debug.flipper.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.flipper.plugin.FlipperPlugin
import com.redmadrobot.debug.flipper.plugin.FlipperPluginContainer
import com.redmadrobot.debug.flipper.ui.data.FlipperItem
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.debug.flipper.R
import kotlinx.coroutines.launch
import com.redmadrobot.debug.panel.common.R as CommonR

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun FlipperFeatureScreen(
    viewModel: FlipperFeaturesViewModel = provideViewModel {
        getPlugin<FlipperPlugin>()
            .getContainer<FlipperPluginContainer>()
            .createFlipperFeaturesViewModel()
    },
) {
    val state by viewModel.state.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var showResetConfirmationDialog by remember { mutableStateOf(false) }

    SourceSelectionDialog(
        state = bottomSheetState,
        sourceSelectionViewModel = provideViewModel {
            getPlugin<FlipperPlugin>()
                .getContainer<FlipperPluginContainer>()
                .createFlipperSourceSelectionViewModel()
        },
        content = {
            FlipperFeatureLayout(
                state,
                onQueryChanged = viewModel::onQueryChanged,
                onValueChanged = viewModel::onFeatureValueChanged,
                onGroupToggleStateChanged = viewModel::onGroupToggleStateChanged,
                onGroupClick = viewModel::onGroupClick,
                onChangeSourceClick = { coroutineScope.launch { bottomSheetState.show() } },
                onResetClick = { showResetConfirmationDialog = true }
            )
        }
    )
    if (showResetConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmationDialog = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.onResetClicked()
                    showResetConfirmationDialog = false
                }) {
                    Text(text = stringResource(id = R.string.flipper_plugin_clear_changes))
                }
            },
            dismissButton = {
                Button(onClick = { showResetConfirmationDialog = false }) {
                    Text(text = stringResource(id = R.string.flipper_plugin_cancel))
                }
            },
            text = { Text(text = stringResource(id = R.string.flipper_plugin_dialog_title_feature_toggles_reset)) }
        )
    }
}

@Composable
internal fun FlipperFeatureLayout(
    state: FlipperFeaturesViewState,
    onQueryChanged: (String) -> Unit,
    onValueChanged: (String, FlipperValue) -> Unit,
    onGroupToggleStateChanged: (String, Boolean) -> Unit,
    onGroupClick: (String) -> Unit,
    onChangeSourceClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Filter(onQueryChanged = onQueryChanged)
            LazyColumn(contentPadding = PaddingValues(bottom = 60.dp)) {
                items(state.items) { item ->
                    when {
                        item is FlipperItem.Group -> {
                            GroupItem(
                                group = item,
                                onGroupToggleStateChanged = onGroupToggleStateChanged,
                                onGroupClick = onGroupClick
                            )
                        }

                        item is FlipperItem.Feature && item.value is FlipperValue.BooleanValue -> {
                            BooleanValueItem(feature = item, onValueChanged = onValueChanged)
                        }

                        item is FlipperItem.Feature && item.value is FlipperValue.StringValue -> {
                            StringValueItem(feature = item, onValueChanged = onValueChanged)
                        }
                    }
                    Divider()
                }
            }
        }
        Box(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Button(modifier = Modifier.height(44.dp), onClick = onChangeSourceClick) {
                Text(text = stringResource(id = R.string.flipper_plugin_button_switch_source).uppercase())
            }
            Button(
                modifier = Modifier
                    .height(44.dp)
                    .align(Alignment.CenterEnd),
                onClick = onResetClick
            ) {
                Text(text = stringResource(id = R.string.flipper_plugin_clear_changes).uppercase())
            }
        }
    }
}

@Composable
private fun Filter(onQueryChanged: (String) -> Unit) {
    var filterValue by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { hasFocus = it.hasFocus },
        value = filterValue,
        onValueChange = {
            filterValue = it
            onQueryChanged(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.flipper_plugin_filter))
        },
        trailingIcon = {
            if (!hasFocus) {
                IconButton(onClick = { focusRequester.requestFocus() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_search),
                        contentDescription = null
                    )
                }
            } else {
                IconButton(onClick = {
                    filterValue = ""
                    onQueryChanged("")
                    focusManager.clearFocus(true)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_clear),
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
private fun GroupItem(
    group: FlipperItem.Group,
    onGroupToggleStateChanged: (String, Boolean) -> Unit,
    onGroupClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = CommonR.color.super_light_gray))
            .clickable { onGroupClick(group.name) }
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = group.name,
            color = if (group.editable) Color.Black else Color.Gray,
            fontWeight = FontWeight.Bold
        )
        Switch(
            modifier = Modifier.align(Alignment.CenterEnd),
            checked = group.allEnabled,
            onCheckedChange = { onGroupToggleStateChanged(group.name, it) },
            enabled = group.editable,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.secondary)
        )
    }
}


@Composable
private fun StringValueItem(
    feature: FlipperItem.Feature,
    onValueChanged: (String, FlipperValue) -> Unit
) {
    var value by remember { mutableStateOf((feature.value as FlipperValue.StringValue).value) }
    var hasFocus by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = feature.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = if (feature.editable) Color.Black else Color.Gray
            )
            if (hasFocus) {
                Button(onClick = {
                    onValueChanged(feature.id, FlipperValue.StringValue(value))
                    focusManager.clearFocus(force = true)
                }) {
                    Text(text = stringResource(id = R.string.flipper_plugin_update))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { hasFocus = it.hasFocus },
            value = value,
            onValueChange = { value = it },
            label = { Text(text = stringResource(id = R.string.flipper_plugin_value)) },
            enabled = feature.editable,
        )
    }
}

@Composable
private fun BooleanValueItem(
    feature: FlipperItem.Feature,
    onValueChanged: (String, FlipperValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = (feature.value as FlipperValue.BooleanValue).value,
            onCheckedChange = { onValueChanged(feature.id, FlipperValue.BooleanValue(it)) },
            enabled = feature.editable
        )
        Text(
            text = feature.description,
            color = if (feature.editable) Color.Black else Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SourceSelectionDialog(
    content: @Composable () -> Unit,
    state: ModalBottomSheetState,
    sourceSelectionViewModel: SourceSelectionViewModel
) {
    val data by sourceSelectionViewModel.sources.collectAsState()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = MaterialTheme.shapes.small,
        sheetContent = {
            Spacer(modifier = Modifier.height(1.dp))
            LazyColumn {
                items(data) { source ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(modifier = Modifier.weight(1F), text = source.name)
                            Checkbox(
                                checked = source.selected,
                                onCheckedChange = { sourceSelectionViewModel.onSelectSource(source.name) })
                        }
                        Divider()
                    }
                }
            }
        },
        content = content
    )
}

@Preview
@Composable
private fun GroupItemPreview() {
    GroupItem(
        group = FlipperItem.Group(
            "Name",
            allEnabled = true,
            editable = false
        ),
        onGroupToggleStateChanged = { _: String, _: Boolean -> },
        onGroupClick = {},
    )
}

@Preview
@Composable
private fun BooleanItemPreview() {
    BooleanValueItem(
        feature = FlipperItem.Feature(
            "id",
            FlipperValue.BooleanValue(true),
            description = "Boolean",
            editable = false
        ),
        onValueChanged = { _: String, _: FlipperValue -> }
    )
}

@Preview
@Composable
private fun StringItemPreview() {
    StringValueItem(
        feature = FlipperItem.Feature(
            "id",
            FlipperValue.StringValue("Value"),
            description = "StringToggle",
            editable = false,
        ),
        onValueChanged = { _: String, _: FlipperValue -> }
    )
}

@Preview
@Composable
private fun FilterPreview() {
    Filter(onQueryChanged = {})
}
