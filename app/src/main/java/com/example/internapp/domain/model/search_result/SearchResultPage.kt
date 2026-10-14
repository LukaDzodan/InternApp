package com.example.internapp.domain.model.search_result


data class SearchResultPage(
    val from: Int,
    val maxFrom: Int,
    val results: List<SearchResult>,
    val size: Int,
    val total: Int
)
