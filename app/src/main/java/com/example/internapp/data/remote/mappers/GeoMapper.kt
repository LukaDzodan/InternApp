package com.example.internapp.data.remote.mappers

import com.example.internapp.data.remote.dto.GeoCoordinates
import com.example.internapp.domain.model.GeoLocation

interface GeoMapper {
    fun toGeoLocation(geoCoordinates: GeoCoordinates): GeoLocation
}

class GeoMapperImpl : GeoMapper {
    override fun toGeoLocation(geoCoordinates: GeoCoordinates): GeoLocation {
        return GeoLocation(
            latitude = geoCoordinates.latitude,
            longitude = geoCoordinates.longitude
        )
    }
}
