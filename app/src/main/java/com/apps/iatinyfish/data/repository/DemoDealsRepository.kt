package com.apps.iatinyfish.data.repository

import com.apps.iatinyfish.data.model.ProductOffer

class DemoDealsRepository : DealsRepository {
    override suspend fun search(query: String): List<ProductOffer> = listOf(
        ProductOffer(
            id = "demo-1",
            store = "Pixel Store",
            productName = query.ifBlank { "Nintendo Switch OLED" },
            price = "Ver en fuente",
            sourceUrl = "https://example.com",
            summary = "Resultado de demostración para validar la interfaz.",
            tags = listOf("Resultado web"),
        ),
        ProductOffer(
            id = "demo-2",
            store = "Game Planet",
            productName = query.ifBlank { "Nintendo Switch OLED" },
            price = "Ver en fuente",
            sourceUrl = "https://example.com",
            summary = "Los datos remotos se activarán después del despliegue.",
            tags = listOf("Demo"),
        ),
    )
}
