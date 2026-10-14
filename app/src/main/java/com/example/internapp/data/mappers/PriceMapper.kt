package com.example.internapp.data.mappers

import com.example.internapp.data.dto.Buy
import com.example.internapp.data.dto.Rent
import com.example.internapp.domain.model.price.BuyPrice
import com.example.internapp.domain.model.price.PriceArea
import com.example.internapp.domain.model.price.PriceInterval
import com.example.internapp.domain.model.price.RentPrice

interface PriceMapper {
    fun toBuyPrice(dto: Buy?): BuyPrice?
    fun toRentPrice(dto: Rent?): RentPrice?
}

class PriceMapperImpl : PriceMapper {
    override fun toBuyPrice(dto: Buy?): BuyPrice? {
        if(dto == null) return null

        return BuyPrice(
            area = dto.area?.let {toPriceArea(dto.area)},
            interval = dto.interval?.let { toPriceInterval(dto.interval)},
            price = dto.price
        )
    }

    //can be changed if rent get fields in the future
    override fun toRentPrice(dto: Rent?): RentPrice? {
        return null
    }

    private fun toPriceArea(area: String?): PriceArea {
        return when (area) {
            "ALL" -> PriceArea.ALL
            else -> PriceArea.UNKNOWN
        }
    }

    private fun toPriceInterval(interval : String) : PriceInterval {
        return when (interval) {
            "ONETIME" -> PriceInterval.ONETIME
            else -> PriceInterval.UNKNOWN
        }
    }

}
