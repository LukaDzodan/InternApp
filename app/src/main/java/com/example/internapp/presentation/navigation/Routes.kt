package com.example.internapp.presentation.navigation

object Routes {

    const val LISTING_SCREEN = "listing_screen_route"
    const val DETAIL_SCREEN = "detail_screen"
    const val FAVORITE_SCREEN = "favorite_screen"

    fun detail(propertyId: String) = "$DETAIL_SCREEN/$propertyId"

}
