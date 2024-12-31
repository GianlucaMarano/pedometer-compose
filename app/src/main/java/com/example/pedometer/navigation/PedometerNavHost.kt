package com.example.pedometer.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.pedometer.feature.analytics.navigation.analyticsScreen
import com.example.pedometer.feature.easteregg.navigation.EGScreen
import com.example.pedometer.feature.home.navigation.homeNavigationRoute
import com.example.pedometer.feature.home.navigation.homeScreen

@Composable
fun PedometerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = homeNavigationRoute,
        modifier = modifier
    ) {
        homeScreen()
        EGScreen()
        analyticsScreen()
    }
}