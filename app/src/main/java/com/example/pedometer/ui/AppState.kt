package com.example.pedometer.ui

import androidx.compose.runtime.Stable
import com.example.pedometer.feature.home.navigation.homeNavigationRoute
import com.example.pedometer.navigation.TopLevelDestination


@Stable
data class AppState(
    val currentDestination: String? = homeNavigationRoute,
    val bottomBarDestinations: List<TopLevelDestination> = TopLevelDestination.entries,
    val isHomeDestination: Boolean = true,
    var permissionsGranted: Boolean = false,
    val isCurrentDestinationTopLevel: Boolean = true,
    val navigateTo: NavigateIntent? = null,
)