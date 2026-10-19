package com.example.internapp.mappers

import com.example.internapp.data.remote.dto.GeoCoordinates
import com.example.internapp.data.remote.mappers.GeoMapperImpl
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GeoMapperImplTest {

    private val mapper = GeoMapperImpl()

    @Test
    fun `toGeoLocation maps latitude and longitude`() {
        val dto = GeoCoordinates(
            longitude = 123.000,
            latitude = 456.000
        )
        val result = mapper.toGeoLocation(dto)
        assertEquals(123.000, result.longitude)
        assertEquals(456.000, result.latitude)
    }

    @Test
    fun `toGeoLocation maps negative coordinates`() {
        val dto = GeoCoordinates(latitude = -47.37, longitude = -8.54)
        val result = mapper.toGeoLocation(dto)
        assertEquals(-47.37, result.latitude)
        assertEquals(-8.54, result.longitude)
    }

    @Test
    fun `toGeoLocation maps zero coordinates`() {
        val dto = GeoCoordinates(latitude = 0.0, longitude = 0.0)
        val result = mapper.toGeoLocation(dto)
        assertEquals(0.0, result.latitude)
        assertEquals(0.0, result.longitude)
    }


}
