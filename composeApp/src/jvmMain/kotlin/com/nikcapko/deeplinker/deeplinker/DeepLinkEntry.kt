package com.nikcapko.deeplinker.deeplinker

import kotlinx.serialization.Serializable

@Serializable
data class DeepLinkEntry(
    val url: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
