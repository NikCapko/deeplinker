package com.nikcapko.deeplinker.deeplinker.utils

import com.nikcapko.deeplinker.deeplinker.models.AdbDevice

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
            val process = ProcessBuilder("adb", "devices", "-l")
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

    fun getAndroidVersion(serial: String): String? {
        return try {
            val process = ProcessBuilder(
                "adb",
                "-s",
                serial,
                "shell",
                "getprop",
                "ro.build.version.release",
            )
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText().trim()
            return output
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseAdbDevicesOutput(output: String): List<AdbDevice> {
        val devices = mutableListOf<AdbDevice>()
        val lines = output.lines()
        for (line in lines) {
            if (line.contains("List of devices attached")) continue
            if (line.isBlank()) continue
            if (line.startsWith("*") || line.startsWith("adb")) continue

            val parts = line.split(Regex("\\s+"))
            val serial = parts[0]
            val model = parts.find { it.startsWith("model:") }?.substringAfter("model:")?.replace("_", "")
            val android = getAndroidVersion(serial)
            devices.add(
                AdbDevice(
                    serial = serial,
                    model = model ?: "Unknown",
                    android = android ?: "?",
                    status = parts[1],
                ),
            )
        }
        return devices
    }
}