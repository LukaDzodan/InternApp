package com.example.internapp.data.remote

import com.example.internapp.data.remote.dto.ListingsResponse
import retrofit2.http.GET

interface Api {

    @GET("properties")
    suspend fun getListings() : ListingsResponse

}
