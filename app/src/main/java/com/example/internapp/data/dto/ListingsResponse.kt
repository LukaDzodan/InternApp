package com.example.internapp.data.dto

data class ListingsResponse(
    val from: Int,
    val maxFrom: Int,
    val results: List<Result>,
    val size: Int,
    val total: Int
)
