package com.nikcapko.deeplinker.deeplinker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    DialogWindow(
        onCloseRequest = onCancel,
        state = rememberDialogState(
            width = 300.dp,
            height = 170.dp,
        )
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
                if (title.isNotBlank()) {
                    Text(title, style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(8.dp))
                }

                if (message.isNotBlank()) {
                    Text(message, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                        onClick = onCancel,
                    ) {
                        Text("Нет")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                        onClick = onConfirm,
                    ) {
                        Text("Да")
                    }
                }
            }
        }
    }
}
