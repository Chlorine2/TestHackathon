package com.example.compose.rally.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun <T: Any> ChangeAmount(
    item: T,
    amountGetter: (T) -> Float,
    amountSetter: (T, Float) -> Unit,
    onCloseDialog: () -> Unit
) {
    var newAmount by remember { mutableStateOf(amountGetter(item).toString()) }

    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = { Text("Change ${item::class.simpleName} Amount") },
        text = {
            Column {
                Text("Current Amount: ${amountGetter(item)}")
                TextField(
                    value = newAmount,
                    onValueChange = { newAmount = it },
                    label = { Text("New Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val updatedAmount = newAmount.toFloatOrNull()
                            if (updatedAmount != null) {
                                amountSetter(item, updatedAmount)
                            }
                            onCloseDialog()
                        }
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedAmount = newAmount.toFloatOrNull()
                    if (updatedAmount != null) {
                        amountSetter(item, updatedAmount)
                    }
                    onCloseDialog()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onCloseDialog
            ) {
                Text("Cancel")
            }
        }
    )
}
