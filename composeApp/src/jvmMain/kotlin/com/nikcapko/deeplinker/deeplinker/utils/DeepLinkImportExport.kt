package com.nikcapko.deeplinker.deeplinker.utils

import com.nikcapko.deeplinker.deeplinker.DeepLinkEntry
import kotlinx.serialization.json.Json
import java.io.File

object DeepLinkImportExport {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    fun exportToFile(entry: DeepLinkEntry, file: File) {
        file.writeText(json.encodeToString(entry))
    }

    fun importFromFile(file: File): DeepLinkEntry {
        val content = file.readText()
        return if (content.isBlank()) {
            DeepLinkEntry()
        } else {
            json.decodeFromString(content)
        }
    }
}
