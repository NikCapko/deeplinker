package com.nikcapko.deeplinker.deeplinker.utils

import com.nikcapko.deeplinker.deeplinker.models.DeepLinkEntry
import kotlinx.serialization.json.Json
import java.io.File

object DeepLinkStorage {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    private val storageFile = File(System.getProperty("user.home"), ".deeplinker/history.json")

    init {
        storageFile.parentFile.mkdirs()
        if (!storageFile.exists()) {
            storageFile.writeText(json.encodeToString(DeepLinkEntry()))
        }
    }

    fun loadEntry(): DeepLinkEntry {
        return try {
            val content = storageFile.readText()
            if (content.isBlank()) {
                DeepLinkEntry()
            } else {
                json.decodeFromString(content)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DeepLinkEntry()
        }
    }

    fun saveEntry(entry: DeepLinkEntry) {
        try {
            storageFile.writeText(json.encodeToString(entry))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}