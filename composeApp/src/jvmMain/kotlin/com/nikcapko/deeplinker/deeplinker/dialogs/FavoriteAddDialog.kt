package com.nikcapko.deeplinker.deeplinker.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.nikcapko.deeplinker.deeplinker.icons.Cross

@Composable
fun FavoriteNameDialog(
    onDismiss: () -> Unit,
    onSave: (name: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    DialogWindow(
        onCloseRequest = onDismiss,
        state = rememberDialogState(
            width = 500.dp,
            height = 250.dp,
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Добавить в избранное", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    trailingIcon = {
                        if (name.isNotBlank()) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable { name = "" },
                                imageVector = Cross,
                                contentDescription = "clear input",
                            )
                        }
                    }
                )

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand),
                        onClick = onDismiss,
                    ) {
                        Text("Отмена")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand),
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(name.trim())
                            }
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}
