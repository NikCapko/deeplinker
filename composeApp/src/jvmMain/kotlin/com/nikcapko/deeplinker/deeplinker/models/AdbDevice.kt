package com.nikcapko.deeplinker.deeplinker.models

data class AdbDevice(
    val id: String,
    val status: String // "device", "offline", "unauthorized" и т.д.
) {
    val isOnline: Boolean get() = status == "device"
}
