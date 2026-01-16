package com.nikcapko.deeplinker.deeplinker.models

import kotlinx.serialization.Serializable

@Serializable
data class DeepLinkEntry(
    val history: List<String> = emptyList(),
    val favorites: List<FavoriteItem> = emptyList(),
) {
    @Serializable
    data class FavoriteItem(val name: String, val deeplink: String)
}