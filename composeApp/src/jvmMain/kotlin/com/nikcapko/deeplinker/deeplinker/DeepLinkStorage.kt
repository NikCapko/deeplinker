package com.nikcapko.deeplinker.deeplinker

import kotlinx.serialization.json.Json
import java.io.File

object DeepLinkStorage {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    private val storageFile = File(System.getProperty("user.home"), ".deeplinker/history.json")

    init {
        storageFile.parentFile.mkdirs()
        if (!storageFile.exists()) storageFile.writeText("[]")
    }

    fun loadEntries(): List<DeepLinkEntry> {
        return try {
            val content = storageFile.readText()
            if (content.isBlank()) emptyList()
            else json.decodeFromString(content)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveEntries(entries: List<DeepLinkEntry>) {
        try {
            storageFile.writeText(json.encodeToString(entries))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}