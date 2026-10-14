package com.example.internapp.domain.model.price

data class BuyPrice(
    val area: PriceArea?,
    val interval: PriceInterval?,
    val price: Long
)
