package com.example.internapp.data.remote.mappers

import com.example.internapp.domain.model.GeoLocation

interface GeoMapper {
    fun toGeoLocation(geoCoordinates: com.example.internapp.data.remote.dto.GeoCoordinates): GeoLocation
}

class GeoMapperImpl : com.example.internapp.data.remote.mappers.GeoMapper {
    override fun toGeoLocation(geoCoordinates: com.example.internapp.data.remote.dto.GeoCoordinates): GeoLocation {
        return GeoLocation(
            latitude = geoCoordinates.latitude,
            longitude = geoCoordinates.longitude
        )
    }
}
