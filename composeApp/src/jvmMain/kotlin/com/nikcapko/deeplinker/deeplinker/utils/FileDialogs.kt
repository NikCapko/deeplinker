package com.nikcapko.deeplinker.deeplinker.utils

import java.awt.FileDialog
import java.awt.Frame
import java.io.File

object FileDialogs {
    fun showSaveDialog(defaultName: String = "deeplinks.json"): File? {
        return try {
            val dialog = FileDialog(Frame(), "Сохранить как...", FileDialog.SAVE)
            dialog.file = defaultName
            dialog.isVisible = true
            val file = dialog.file?.let { File(dialog.directory, it) }
            dialog.dispose()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun showOpenDialog(): File? {
        return try {
            val dialog = FileDialog(Frame(), "Открыть файл...", FileDialog.LOAD)
            dialog.file = "*.json"
            dialog.isVisible = true
            val file = dialog.file?.let { File(dialog.directory, it) }
            dialog.dispose()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
