package com.example.internapp.domain.model.property

import com.example.internapp.domain.model.price.BuyPrice
import com.example.internapp.domain.model.price.RentPrice


data class PropertyPrices(
    val buy: BuyPrice?,
    val currency: String,
    val rent: RentPrice?
)
