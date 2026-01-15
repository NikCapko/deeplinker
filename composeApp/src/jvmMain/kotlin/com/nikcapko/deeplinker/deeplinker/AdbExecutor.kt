package com.nikcapko.deeplinker.deeplinker

import java.io.BufferedReader
import java.io.InputStreamReader

object AdbExecutor {
    fun launchDeepLink(url: String): String? {
        return try {
            val process = ProcessBuilder("adb", "shell", "am", "start", "-W", "-a", "android.intent.action.VIEW", "-d", url)
                .redirectErrorStream(true)
                .start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readLines().joinToString("\n")
            process.waitFor()
            output
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }
}
