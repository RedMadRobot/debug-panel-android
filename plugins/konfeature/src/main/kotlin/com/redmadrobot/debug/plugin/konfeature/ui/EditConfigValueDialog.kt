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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.R
import com.redmadrobot.debug.plugin.konfeature.ui.data.EditDialogState

@Composable
internal fun EditConfigValueDialog(
    state: EditDialogState,
    onValueChanged: (key: String, value: Any) -> Unit,
    onValueReset: (key: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val initialValue = state.value
    var value by remember { mutableStateOf(state.value) }
    var isInputEmpty by remember { mutableStateOf(false) }

    AlertDialog(
        backgroundColor = colorResource(id = R.color.super_light_gray),
        title = {
            Text(text = "Edit: ${state.key}")
        },
        text = {
            when (initialValue) {
                is Boolean -> BooleanEditInput(initialValue, onValueChanged = { value = it })
                is Long -> LongEditInput(
                    initialValue,
                    onValueChanged = { value = it },
                    onEmpty = { isInputEmpty = it }
                )

                is Double -> DoubleEditInput(
                    initialValue,
                    onValueChanged = { value = it },
                    onEmpty = { isInputEmpty = it }
                )

                is String -> StringEditInput(initialValue, onValueChanged = { value = it })
            }
        },
        onDismissRequest = onDismissRequest,
        buttons = {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Button(onClick = onDismissRequest) {
                    Text(text = "Close")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    enabled = !isInputEmpty && initialValue != value,
                    onClick = {
                        onValueChanged.invoke(state.key, value)
                        onDismissRequest.invoke()
                    }
                ) {
                    Text(text = "Save")
                }
                if (state.isDebugSource) {
                    Button(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = {
                            onValueReset.invoke(state.key)
                            onDismissRequest.invoke()
                        }
                    ) {
                        Text(text = "Reset")
                    }
                }
            }
        }
    )
}

@Composable
private fun BooleanEditInput(
    value: Boolean,
    onValueChanged: (Any) -> Unit
) {
    var checked by remember { mutableStateOf(value) }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = "Boolean value:"
        )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                onValueChanged.invoke(it)
            }
        )
    }
}

@Composable
private fun LongEditInput(
    value: Long,
    onValueChanged: (Any) -> Unit,
    onEmpty: (Boolean) -> Unit,
) {
    var text by remember { mutableStateOf(value.toString(10)) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Long value:") },
        value = text,
        onValueChange = {
            val newValue = it.toLongOrNull()
            if (newValue != null || it.isEmpty()) {
                text = it
                newValue?.let(onValueChanged)
                onEmpty.invoke(it.isEmpty())
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun DoubleEditInput(
    value: Double,
    onValueChanged: (Any) -> Unit,
    onEmpty: (Boolean) -> Unit,
) {
    var text by remember { mutableStateOf(value.toBigDecimal().toPlainString()) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Double value:") },
        value = text,
        onValueChange = {
            val newValue = it.toDoubleOrNull()
            if (newValue != null || it.isEmpty()) {
                text = it
                newValue?.let(onValueChanged)
                onEmpty.invoke(it.isEmpty())
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun StringEditInput(
    value: String,
    onValueChanged: (Any) -> Unit,
) {
    var text by remember { mutableStateOf(value) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "String value:") },
        value = text,
        onValueChange = {
            text = it
            onValueChanged.invoke(it)
        },
    )
}
