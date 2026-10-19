package com.example.internapp.mappers

import com.example.internapp.data.remote.dto.Buy
import com.example.internapp.data.remote.mappers.PriceMapperImpl
import com.example.internapp.domain.model.price.PriceArea
import com.example.internapp.domain.model.price.PriceInterval
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class PriceMapperImplTest {

    private val mapper = PriceMapperImpl()

    @Test
    fun `toBuyPrice maps price and interval`() {
        val dto = Buy(price = 500_000L, area = "ALL", interval = "ONETIME")
        val result = mapper.toBuyPrice(dto)
        assertEquals(500_000L, result!!.price)
        assertEquals(PriceArea.ALL, result.area)
        assertEquals(PriceInterval.ONETIME, result.interval)
    }

    @Test
    fun `toBuyPrice returns null when dto is null`() {
        assertNull(mapper.toBuyPrice(null))
    }

    @Test
    fun `unknown interval maps to UNKNOWN`() {
        val dto = Buy(price = 1L, area = null, interval = "WEIRD")
        assertEquals(PriceInterval.UNKNOWN, mapper.toBuyPrice(dto)!!.interval)
    }
}
