package com.plcoding.stockmarketapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plcoding.stockmarketapp.presentation.company_listings.CompanyListingScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CompanyListingScreen.route) {

        composable(
            route = Screen.CompanyListingScreen.route
        ) {
            CompanyListingScreen(navController = navController)
        }
    }
}