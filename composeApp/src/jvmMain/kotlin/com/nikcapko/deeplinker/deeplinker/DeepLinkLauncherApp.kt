package com.nikcapko.deeplinker.deeplinker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nikcapko.deeplinker.deeplinker.icons.Cross
import com.nikcapko.deeplinker.deeplinker.icons.Export
import com.nikcapko.deeplinker.deeplinker.icons.Import
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DeepLinkLauncherApp() {
    var inputUrl by remember { mutableStateOf("") }
    var logOutput by remember { mutableStateOf("") }
    var entry by remember { mutableStateOf(DeepLinkEntry()) }

    var showFavoriteDialog by remember { mutableStateOf(false) }
    var urlToFavorite by remember { mutableStateOf("") }

    var showDeleteAllHistory by remember { mutableStateOf(false) }
    var showDeleteAllFavorite by remember { mutableStateOf(false) }

    var showDeleteHistoryItem by remember { mutableStateOf(false) }
    var historyItemToDelete by remember { mutableStateOf<DeepLinkEntry.HistoryItem?>(null) }

    var showDeleteFavoriteItem by remember { mutableStateOf(false) }
    var favoriteItemToDelete by remember { mutableStateOf<DeepLinkEntry.FavoriteItem?>(null) }

    LaunchedEffect(Unit) {
        entry = withContext(Dispatchers.IO) {
            DeepLinkStorage.loadEntry()
        }
    }

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {

                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Import,
                        contentDescription = "import file",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Импортировать файл")
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {

                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Export,
                        contentDescription = "export file",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Экспортировать файл")
                }
            }
        }

        // Поле ввода
        OutlinedTextField(
            value = inputUrl,
            onValueChange = { inputUrl = it },
            label = { Text("Введите deeplink") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                if (inputUrl.isNotBlank()) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .clickable {
                                inputUrl = ""
                            },
                        imageVector = Cross,
                        contentDescription = "clear input",
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки
        Row {
            Button(
                onClick = {
                    if (inputUrl.isNotBlank()) {
                        scope.launch {
                            val result = withContext(Dispatchers.IO) {
                                AdbExecutor.launchDeepLink(inputUrl.trim())
                            }
                            logOutput = result ?: "Запущено"
                            // Сохраняем в историю
                            entry = entry.copy(
                                history = entry.history
                                    .filter { it.link != inputUrl }
                                    .toMutableList()
                                    .apply {
                                        add(0, DeepLinkEntry.HistoryItem(inputUrl))
                                    }
                            )
                            withContext(Dispatchers.IO) {
                                DeepLinkStorage.saveEntry(entry)
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Запустить")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (inputUrl.isNotBlank()) {
                        urlToFavorite = inputUrl
                        showFavoriteDialog = true
                    }
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Text("★")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Лог (опционально)
        if (logOutput.isNotBlank()) {
            Text("Результат:\n$logOutput", modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "История",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        modifier = Modifier.clickable {
                            showDeleteAllHistory = true
                        },
                        text = "Очистить",
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue,
                    )
                }
                LazyColumn {
                    items(entry.history) { item ->
                        DeepLinkHistoryItem(
                            item = item,
                            onClick = { inputUrl = item.link },
                            onDeleteClick = {
                                historyItemToDelete = item
                                showDeleteHistoryItem = true
                            },
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Избранное",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        modifier = Modifier.clickable {
                            showDeleteAllFavorite = true
                        },
                        text = "Удалить все",
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue,
                    )
                }
                LazyColumn {
                    items(entry.favorite) { item ->
                        DeepLinkFavoriteItem(
                            item = item,
                            onClick = { inputUrl = item.link },
                            onDeleteClick = {
                                favoriteItemToDelete = item
                                showDeleteFavoriteItem = true
                            },
                        )
                    }
                }
            }
        }
    }

    if (showFavoriteDialog) {
        FavoriteNameDialog(
            onDismiss = { showFavoriteDialog = false },
            onSave = { name ->
                showFavoriteDialog = false
                val favorite = DeepLinkEntry.FavoriteItem(name, urlToFavorite)

                // Обновляем список
                entry = entry.copy(
                    favorite = entry.favorite.toMutableList().apply {
                        add(0, favorite)
                    }
                )

                scope.launch(Dispatchers.IO) {
                    DeepLinkStorage.saveEntry(entry)
                }
            },
        )
    }

    if (showDeleteAllHistory) {
        ConfirmDialog(
            title = "",
            message = "Вы уверены, что хотите очистить историю?",
            onCancel = { showDeleteAllHistory = false },
            onConfirm = {
                showDeleteAllHistory = false

                // Обновляем список
                entry = entry.copy(
                    history = emptyList(),
                )

                scope.launch(Dispatchers.IO) {
                    DeepLinkStorage.saveEntry(entry)
                }
            },
        )
    }

    if (showDeleteAllFavorite) {
        ConfirmDialog(
            title = "",
            message = "Вы уверены, что хотите удалить все диплинки из избранного?",
            onCancel = { showDeleteAllFavorite = false },
            onConfirm = {
                showDeleteAllFavorite = false

                // Обновляем список
                entry = entry.copy(
                    favorite = emptyList(),
                )

                scope.launch(Dispatchers.IO) {
                    DeepLinkStorage.saveEntry(entry)
                }
            },
        )
    }

    if (showDeleteFavoriteItem) {
        favoriteItemToDelete?.let { item ->
            ConfirmDialog(
                title = "Удалить избранное:",
                message = "${item.name} → ${item.link}",
                onCancel = { showDeleteFavoriteItem = false },
                onConfirm = {
                    showDeleteFavoriteItem = false

                    // Обновляем список
                    entry = entry.copy(
                        favorite = entry.favorite.toMutableList().apply {
                            remove(item)
                        },
                    )

                    scope.launch(Dispatchers.IO) {
                        DeepLinkStorage.saveEntry(entry)
                    }
                },
            )
        }
    }
}

@Composable
fun DeepLinkHistoryItem(
    item: DeepLinkEntry.HistoryItem,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = item.link,
                maxLines = 1,
            )
//            Icon(
//                modifier = Modifier
//                    .size(24.dp)
//                    .clip(CircleShape)
//                    .clickable { onDeleteClick() },
//                imageVector = Cross,
//                contentDescription = "clear input",
//            )
        }
    }
}

@Composable
fun DeepLinkFavoriteItem(
    item: DeepLinkEntry.FavoriteItem,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "${item.name} → ${item.link}",
                maxLines = 1,
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onDeleteClick() },
                imageVector = Cross,
                contentDescription = "clear input",
            )
        }
    }
}
