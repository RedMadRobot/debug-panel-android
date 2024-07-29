package com.redmadrobot.debug.plugin.servers.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import com.redmadrobot.debug.core.extension.OnLifecycleEvent
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.ServersPluginContainer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun ServersScreen(
    viewModel: ServersViewModel = provideViewModel {
        getPlugin<ServersPlugin>()
            .getContainer<ServersPluginContainer>()
            .createServersViewModel()
    },
    isEditMode: Boolean
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            if (isEditMode) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.onAddClicked() },
                    text = { Text("Add") },
                    icon = {
                        Icon(
                            painterResource(R.drawable.icon_add_server),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) {
        ServersScreenLayout(
            state = state,
            isEditMode = isEditMode,
            onServerClick = if (!isEditMode) viewModel::onServerClicked else viewModel::onEditServerClicked,
            onStageClick = viewModel::onStageClicked,
            onServerDeleteClick = viewModel::onRemoveServerClicked
        )
    }
    if (state.serverDialogState.show) {
        ServerDialog(
            state = state.serverDialogState,
            onNameChange = viewModel::onServerNameChanged,
            onUrlChange = viewModel::onServerUrlChanged,
            onDismiss = viewModel::dismissDialogs,
            onSaveClick = viewModel::onSaveServerClicked,
        )
    }
    OnLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.loadServers()
        }
    }
}

@Composable
private fun ServersScreenLayout(
    state: ServersViewState,
    isEditMode: Boolean,
    onServerClick: (DebugServer) -> Unit,
    onServerDeleteClick: (DebugServer) -> Unit,
    onStageClick: (DebugStage) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 64.dp),
    ) {
        StageItems(
            items = state.preInstalledStages,
            titleRes = R.string.pre_installed_stages,
            isSelectable = !isEditMode,
            onItemClick = onStageClick.takeIf { !isEditMode },
        )
        ServerItems(
            items = state.preInstalledServers,
            titleRes = R.string.pre_installed_servers,
            isSelectable = !isEditMode,
            showDelete = false,
            onItemClick = onServerClick.takeIf { !isEditMode },
            onDeleteClick = onServerDeleteClick,
        )
        StageItems(
            items = state.addedStages,
            titleRes = R.string.added_stages,
            isSelectable = !isEditMode,
            onItemClick = onStageClick,
        )
        ServerItems(
            items = state.addedServers,
            titleRes = R.string.added_servers,
            isSelectable = !isEditMode,
            showDelete = isEditMode,
            onItemClick = onServerClick,
            onDeleteClick = onServerDeleteClick,
        )
    }
}

private fun LazyListScope.ServerItems(
    items: List<ServerItemData>,
    @StringRes titleRes: Int,
    isSelectable: Boolean,
    showDelete: Boolean,
    onDeleteClick: (DebugServer) -> Unit,
    onItemClick: ((DebugServer) -> Unit)? = null,
) {
    if (items.isEmpty()) return

    TitleItem(titleRes)
    items(items) { item ->
        ServerItem(
            server = item.server,
            selected = item.isSelected && isSelectable,
            showDelete = showDelete,
            onItemClick = onItemClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

private fun LazyListScope.StageItems(
    items: List<StageItemData>,
    @StringRes titleRes: Int,
    isSelectable: Boolean,
    onItemClick: ((DebugStage) -> Unit)? = null,
) {
    if (items.isEmpty()) return

    TitleItem(titleRes)
    items(items) { item ->
        StageItem(
            stage = item.server,
            selected = item.isSelected && isSelectable,
            onItemClick = onItemClick,
        )
    }
}

private fun LazyListScope.TitleItem(@StringRes titleRes: Int) {
    item {
        Text(
            text = stringResource(id = titleRes).uppercase(),
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(vertical = 16.dp),
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun ServerItem(
    server: DebugServer,
    selected: Boolean,
    showDelete: Boolean,
    onDeleteClick: (DebugServer) -> Unit,
    onItemClick: ((DebugServer) -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable { onItemClick?.invoke(server) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_router),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Text(server.name, fontWeight = FontWeight.SemiBold)
                Box(modifier = Modifier.weight(1f)) {
                    if (selected) {
                        Icon(
                            painterResource(R.drawable.icon_selected),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    } else if (showDelete) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = { onDeleteClick(server) },
                        ) {
                            Icon(
                                painterResource(R.drawable.icon_delete),
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "url: ${server.url}", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun StageItem(
    stage: DebugStage,
    selected: Boolean,
    onItemClick: ((DebugStage) -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable { onItemClick?.invoke(stage) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_stage),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Text(stage.name, fontWeight = FontWeight.SemiBold)
                Box(modifier = Modifier.weight(1f)) {
                    if (selected) {
                        Icon(
                            painterResource(R.drawable.icon_selected),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                stage.hosts.forEach { (key, value) ->
                    Text(text = "$key: $value", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ServerDialog(
    state: ServerDialogState,
    onNameChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = state.serverName,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(R.string.name)) },
                    isError = state.inputErrors?.nameError != null
                )
                if (state.inputErrors?.nameError != null) {
                    Text(text = stringResource(id = state.inputErrors.nameError), color = Color.Red)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.serverUrl,
                    onValueChange = onUrlChange,
                    label = { Text(stringResource(R.string.server_host_hint)) },
                    isError = state.inputErrors?.urlError != null
                )
                if (state.inputErrors?.urlError != null) {
                    Text(text = stringResource(id = state.inputErrors.urlError), color = Color.Red)
                }
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.save_server).uppercase())
                }
            }
        }
    }
}
