package com.nikcapko.deeplinker.deeplinker.utils

import com.nikcapko.deeplinker.deeplinker.models.AdbDevice
import java.io.BufferedReader
import java.io.InputStreamReader

object AdbExecutor {
    fun launchDeepLink(url: String, deviceId: String? = null): String? {
        val command = buildList {
            add("adb")
            if (deviceId != null) {
                add("-s")
                add(deviceId)
            }
            add("shell")
            add("am")
            add("start")
            add("-W")
            add("-a")
            add("android.intent.action.VIEW")
            add("-d")
            add(url)
        }

        return try {
            val process = ProcessBuilder(command)
                .redirectErrorStream(true)
                .start()

            val result = process.inputStream.bufferedReader().readText()
            process.waitFor()
            result
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }

    fun listDevices(): List<AdbDevice> {
        return try {
            val process = ProcessBuilder("adb", "devices")
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()

            parseAdbDevicesOutput(output)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseAdbDevicesOutput(output: String): List<AdbDevice> {
        val devices = mutableListOf<AdbDevice>()
        val lines = output.lines()
        for (line in lines) {
            if (line.contains("List of devices attached")) continue
            if (line.isBlank()) continue
            if (line.startsWith("*") || line.startsWith("adb")) continue

            val parts = line.split(Regex("\\s+"), limit = 2)
            if (parts.size == 2) {
                val id = parts[0]
                val status = parts[1]
                devices.add(AdbDevice(id, status))
            }
        }
        return devices
    }
}