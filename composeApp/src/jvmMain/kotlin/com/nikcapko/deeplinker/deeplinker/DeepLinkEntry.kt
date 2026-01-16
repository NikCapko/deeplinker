package com.nikcapko.deeplinker.deeplinker

import kotlinx.serialization.Serializable

@Serializable
data class DeepLinkEntry(
    val history: List<HistoryItem> = emptyList(),
    val favorite: List<FavoriteItem> = emptyList(),
) {
    @Serializable
    data class HistoryItem(val link: String)

    @Serializable
    data class FavoriteItem(val name: String, val link: String)
}
