package com.example.internapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.internapp.presentation.detail_screen.DetailScreenRoute
import com.example.internapp.presentation.favorites_screen.FavoriteScreenRoute
import com.example.internapp.presentation.listing_screen.ListingScreenRoute

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.LISTING_SCREEN
    ) {
        composable(
            route = Routes.LISTING_SCREEN
        ) {
            ListingScreenRoute(
                onPropertyClick = { property ->
                    navController.navigate(
                        route = Routes.detail(property.id)
                    )
                },
                onFavoritesClick = {
                    navController.navigate(Routes.FAVORITE_SCREEN)
                }
            )
        }

        composable(
            route = "${Routes.DETAIL_SCREEN}/{${NavArgs.PROPERTY_ID}}",
            arguments = listOf(
                navArgument(NavArgs.PROPERTY_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString(NavArgs.PROPERTY_ID)
                ?: return@composable
            DetailScreenRoute(
                propertyId = propertyId,
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.FAVORITE_SCREEN
        ) {
            FavoriteScreenRoute(
                onBack = { navController.popBackStack() },
                onPropertyClick = { property ->
                    navController.navigate(route = Routes.detail(property.id))
                },
            )
        }

    }

}

object NavArgs {
    const val PROPERTY_ID = "propertyId"
}
