package com.redmadrobot.debug.plugin.accounts.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.accounts.AccountsPlugin
import com.redmadrobot.debug.plugin.accounts.AccountsPluginContainer
import com.redmadrobot.debug.plugin.accounts.R
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun AccountsScreen(
    viewModel: AccountsViewModel = provideViewModel {
        getPlugin<AccountsPlugin>()
            .getContainer<AccountsPluginContainer>()
            .createAccountsViewModel()
    },
    isEditMode: Boolean
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editableAccount by remember { mutableStateOf<DebugAccount?>(null) }

    Scaffold(
        floatingActionButton = {
            if (isEditMode) {
                ExtendedFloatingActionButton(
                    onClick = { showDialog = true },
                    text = { Text("Add") },
                    icon = {
                        Icon(
                            painterResource(R.drawable.icon_add_account),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) {
        AccountScreenLayout(
            state = state,
            isEditMode = isEditMode,
            onSelectedClick = viewModel::setAccountAsCurrent,
            onOpenDialogClick = {
                showDialog = true
                editableAccount = it
            },
            onDeleteClick = viewModel::removeAccount,
        )
        if (showDialog) {
            AccountDetailsDialog(
                onDismiss = {
                    showDialog = false
                    editableAccount = null
                },
                onSaveClick = viewModel::saveAccount,
                account = editableAccount
            )
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.loadAccounts()
    }
}

@Composable
private fun AccountScreenLayout(
    state: AccountsViewState,
    isEditMode: Boolean,
    onSelectedClick: (DebugAccount) -> Unit,
    onDeleteClick: (DebugAccount) -> Unit,
    onOpenDialogClick: (DebugAccount?) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.allItems.forEach { item ->
            item {
                when (item) {
                    is DebugAccountItem.Header -> {
                        Text(
                            item.header.uppercase(),
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(vertical = 16.dp),
                            fontSize = 16.sp
                        )
                    }

                    is DebugAccountItem.PreinstalledAccount -> {
                        AccountItem(
                            item.account,
                            false,
                            onItemClick = { if (!isEditMode) onSelectedClick(it) }
                        )
                    }

                    is DebugAccountItem.AddedAccount -> {
                        AccountItem(
                            account = item.account,
                            showDelete = isEditMode,
                            onItemClick = {
                                if (isEditMode) onOpenDialogClick(it) else onSelectedClick(it)
                            },
                            onDeleteClick = onDeleteClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountItem(
    account: DebugAccount,
    showDelete: Boolean,
    onItemClick: (DebugAccount) -> Unit,
    onDeleteClick: (DebugAccount) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onItemClick(account) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_account),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
                Text(account.login)
            }
            if (showDelete) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { onDeleteClick(account) },
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
}

@Composable
private fun AccountDetailsDialog(
    onDismiss: () -> Unit,
    onSaveClick: (DebugAccount) -> Unit,
    account: DebugAccount? = null,
) {
    var login by remember { mutableStateOf(account?.login.orEmpty()) }
    var password by remember { mutableStateOf(account?.password.orEmpty()) }
    var pin by remember { mutableStateOf(account?.pin.orEmpty()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        label = { Text(stringResource(R.string.login_hint)) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password_hint)) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text(stringResource(R.string.pin)) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val debugAccount = DebugAccount(
                                id = account?.id ?: 0,
                                login = login,
                                password = password,
                                pin = pin
                            )
                            onSaveClick(debugAccount)
                            onDismiss()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.save_account_text).uppercase())
                    }
                }
            }
        }
    }
}

internal sealed class DebugAccountItem {
    internal data class Header(val header: String) : DebugAccountItem()
    internal data class PreinstalledAccount(val account: DebugAccount) : DebugAccountItem()
    internal data class AddedAccount(var account: DebugAccount) : DebugAccountItem()
}


@Preview
@Composable
private fun DialogPreview() {
    AccountDetailsDialog({}, {})
}
