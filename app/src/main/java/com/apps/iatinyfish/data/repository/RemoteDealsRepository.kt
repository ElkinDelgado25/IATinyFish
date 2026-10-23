package com.apps.iatinyfish.data.repository

import com.apps.iatinyfish.data.model.ProductOffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private data class SearchRequest(val query: String)
private data class SearchResponse(val query: String, val offers: List<ProductOffer> = emptyList())

private interface SearchProductsApi {
    @POST("functions/v1/search-products")
    suspend fun search(@Body request: SearchRequest): SearchResponse
}

class RemoteDealsRepository : DealsRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://qbbteihjjhvegqdvfqrh.supabase.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchProductsApi::class.java)

    override suspend fun search(query: String): List<ProductOffer> =
        api.search(SearchRequest(query)).offers
}
