package com.apps.iatinyfish.data.repository

import com.apps.iatinyfish.data.model.ProductOffer

interface DealsRepository {
    suspend fun search(query: String): List<ProductOffer>
}
