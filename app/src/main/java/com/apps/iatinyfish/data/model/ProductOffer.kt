package com.apps.iatinyfish.data.model

data class ProductOffer(
    val id: String,
    val store: String,
    val productName: String,
    val price: String,
    val sourceUrl: String,
    val summary: String,
    val tags: List<String> = emptyList(),
)
