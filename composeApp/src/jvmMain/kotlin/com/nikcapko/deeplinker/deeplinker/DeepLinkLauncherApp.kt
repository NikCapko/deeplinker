package com.nikcapko.deeplinker.deeplinker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nikcapko.deeplinker.deeplinker.icons.Cross
import com.nikcapko.deeplinker.deeplinker.icons.Star
import com.nikcapko.deeplinker.deeplinker.icons.StartFilled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DeepLinkLauncherApp() {
    var inputUrl by remember { mutableStateOf("") }
    var logOutput by remember { mutableStateOf("") }
    var entries by remember { mutableStateOf<List<DeepLinkEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        entries = withContext(Dispatchers.IO) {
            DeepLinkStorage.loadEntries()
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Поле ввода
        OutlinedTextField(
            value = inputUrl,
            onValueChange = { inputUrl = it },
            label = { Text("Введите deeplink") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            inputUrl = ""
                        },
                    imageVector = Cross,
                    contentDescription = "",
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки
        Row {
            Button(
                onClick = {
                    if (inputUrl.isNotBlank()) {
                        scope.launch {
                            val pair = pair(inputUrl, logOutput, entries)
                            entries = pair.first
                            logOutput = pair.second
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
                        val existing = entries.find { it.url == inputUrl.trim() }
                        val newEntries = if (existing != null) {
                            entries.map { if (it.url == inputUrl.trim()) it.copy(isFavorite = !it.isFavorite) else it }
                        } else {
                            val newEntry = DeepLinkEntry(inputUrl.trim(), isFavorite = true)
                            listOf(newEntry) + entries
                        }
                        entries = newEntries
                        scope.launch(Dispatchers.IO) {
                            DeepLinkStorage.saveEntries(newEntries)
                        }
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
                Text("Избранное", fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(entries.filter { it.isFavorite }) { entry ->
                        DeepLinkItem(
                            entry = entry,
                            onClick = { url -> inputUrl = url },
                            onDoubleClick = { url ->
                                inputUrl = url
                                scope.launch {
                                    val pair = pair(inputUrl, logOutput, entries)
                                    entries = pair.first
                                    logOutput = pair.second
                                }
                            },
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text("История", fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(entries.filter { !it.isFavorite }.take(20)) { entry ->
                        DeepLinkItem(
                            entry = entry,
                            onClick = { url -> inputUrl = url },
                            onDoubleClick = { url ->
                                inputUrl = url
                                scope.launch {
                                    val pair = pair(inputUrl, logOutput, entries)
                                    entries = pair.first
                                    logOutput = pair.second
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

private suspend fun pair(
    inputUrl: String,
    logOutput: String,
    entries: List<DeepLinkEntry>
): Pair<List<DeepLinkEntry>, String> {
    var logOutput1 = logOutput
    var entries1 = entries
    val result = withContext(Dispatchers.IO) {
        AdbExecutor.launchDeepLink(inputUrl.trim())
    }
    logOutput1 = result ?: "Запущено"
    // Сохраняем в историю
    val newEntry = DeepLinkEntry(inputUrl.trim(), isFavorite = false)
    val updated = (listOf(newEntry) + entries1).distinctBy { it.url }.take(100)
    entries1 = updated
    withContext(Dispatchers.IO) {
        DeepLinkStorage.saveEntries(updated)
    }
    return Pair(entries1, logOutput1)
}

@Composable
fun DeepLinkItem(
    entry: DeepLinkEntry,
    onClick: (String) -> Unit,
    onDoubleClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(entry.url) }
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (entry.isFavorite) StartFilled else Star,
                contentDescription = null,
                tint = if (entry.isFavorite) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = entry.url, maxLines = 1)
        }
    }
}
