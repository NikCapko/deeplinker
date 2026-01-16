package com.nikcapko.deeplinker.deeplinker.models

data class AdbDevice(
    val serial: String,
    val model: String?,
    val android: String?,
    val status: String // "device", "offline", "unauthorized" и т.д.
) {
    val isOnline: Boolean get() = status == "device"

    fun getInfo() = "$serial | $model | Android $android"
}
