package com.redmadrobot.debug.plugin.konfeature.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.plugin.konfeature.R
import com.redmadrobot.debug.plugin.konfeature.ui.data.EditDialogState
import com.redmadrobot.debug.core.R as CoreR

@Composable
internal fun EditConfigValueDialog(
    state: EditDialogState,
    onValueChange: (key: String, value: Any) -> Unit,
    onValueReset: (key: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val initialValue = state.value
    var value by remember { mutableStateOf(state.value) }
    var isInputEmpty by remember { mutableStateOf(false) }
    val saveEnabled by remember {
        derivedStateOf { !isInputEmpty && initialValue != value }
    }

    AlertDialog(
        backgroundColor = colorResource(id = CoreR.color.super_light_gray),
        title = {
            Text(text = stringResource(id = R.string.konfeature_plugin_edit_dialog_title, state.key))
        },
        text = {
            when (initialValue) {
                is Boolean -> BooleanEditInput(initialValue, onValueChange = { value = it })
                is Long -> LongEditInput(
                    initialValue,
                    onValueChange = { value = it },
                    onEmptyInput = { isInputEmpty = it }
                )

                is Double -> DoubleEditInput(
                    initialValue,
                    onValueChange = { value = it },
                    onEmptyImput = { isInputEmpty = it }
                )

                is String -> StringEditInput(initialValue, onValueChange = { value = it })
            }
        },
        onDismissRequest = onDismissRequest,
        buttons = {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Button(onClick = onDismissRequest) {
                    Text(text = stringResource(id = R.string.konfeature_plugin_close))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    enabled = saveEnabled,
                    onClick = {
                        onValueChange.invoke(state.key, value)
                        onDismissRequest.invoke()
                    }
                ) {
                    Text(text = stringResource(id = R.string.konfeature_plugin_save))
                }
                if (state.isDebugSource) {
                    Button(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = {
                            onValueReset.invoke(state.key)
                            onDismissRequest.invoke()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.konfeature_plugin_reset))
                    }
                }
            }
        }
    )
}

@Composable
private fun BooleanEditInput(
    value: Boolean,
    onValueChange: (Any) -> Unit
) {
    var checked by remember { mutableStateOf(value) }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = stringResource(id = R.string.konfeature_plugin_edit_dialog_hint_boolean)
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { newChecked ->
                checked = newChecked
                onValueChange.invoke(newChecked)
            }
        )
    }
}

@Composable
private fun LongEditInput(
    value: Long,
    onValueChange: (Any) -> Unit,
    onEmptyInput: (Boolean) -> Unit,
) {
    var text by remember { mutableStateOf(value.toString(10)) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.konfeature_plugin_edit_dialog_hint_long)) },
        value = text,
        onValueChange = { newText ->
            val newValue = newText.toLongOrNull()
            if (newValue != null || newText.isEmpty()) {
                text = newText
                newValue?.let(onValueChange)
                onEmptyInput.invoke(newText.isEmpty())
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun DoubleEditInput(
    value: Double,
    onValueChange: (Any) -> Unit,
    onEmptyImput: (Boolean) -> Unit,
) {
    var text by remember { mutableStateOf(value.toBigDecimal().toPlainString()) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.konfeature_plugin_edit_dialog_hint_double)) },
        value = text,
        onValueChange = { newText ->
            val newValue = newText.toDoubleOrNull()
            if (newValue != null || newText.isEmpty()) {
                text = newText
                newValue?.let(onValueChange)
                onEmptyImput.invoke(newText.isEmpty())
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun StringEditInput(
    value: String,
    onValueChange: (Any) -> Unit,
) {
    var text by remember { mutableStateOf(value) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.konfeature_plugin_edit_dialog_hint_string)) },
        value = text,
        onValueChange = { newText ->
            text = newText
            onValueChange.invoke(newText)
        },
    )
}
