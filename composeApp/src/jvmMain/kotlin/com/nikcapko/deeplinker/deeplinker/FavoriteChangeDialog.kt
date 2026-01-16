package com.nikcapko.deeplinker.deeplinker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import com.nikcapko.deeplinker.deeplinker.icons.Cross
import com.nikcapko.deeplinker.deeplinker.models.DeepLinkEntry

@Composable
fun FavoriteChangeDialog(
    deeplink: DeepLinkEntry.FavoriteItem,
    onDismiss: () -> Unit,
    onSave: (deeplink: DeepLinkEntry.FavoriteItem) -> Unit,
) {
    var deeplinkName by remember { mutableStateOf(deeplink.name) }
    var deeplinkUrl by remember { mutableStateOf(deeplink.deeplink) }

    DialogWindow(onCloseRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Изменить избранное", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = deeplinkName,
                    onValueChange = { deeplinkName = it },
                    label = { Text("Название") },
                    trailingIcon = {
                        if (deeplinkName.isNotBlank()) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        deeplinkName = ""
                                    },
                                imageVector = Cross,
                                contentDescription = "clear deeplink name input",
                            )
                        }
                    }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = deeplinkUrl,
                    onValueChange = { deeplinkUrl = it },
                    label = { Text("Deeplink URL") },
                    trailingIcon = {
                        if (deeplinkUrl.isNotBlank()) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        deeplinkUrl = ""
                                    },
                                imageVector = Cross,
                                contentDescription = "clear deeplink url input",
                            )
                        }
                    }
                )

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (deeplinkName.isNotBlank() && deeplinkUrl.isNotBlank()) {
                                onSave(DeepLinkEntry.FavoriteItem(deeplinkName, deeplinkUrl))
                            }
                        },
                        enabled = deeplinkName.isNotBlank() && deeplinkUrl.isNotBlank()
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}
