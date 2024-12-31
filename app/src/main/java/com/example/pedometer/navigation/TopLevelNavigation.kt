package com.example.pedometer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pedometer.R
import com.example.pedometer.feature.analytics.navigation.analyticsNavigationRoute
import com.example.pedometer.feature.home.navigation.homeNavigationRoute

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
    val route: String
) {
    HOME(
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.home,
        titleTextId = R.string.app_name,
        route = homeNavigationRoute
    ),
    ANALYTICS(
        selectedIcon = Icons.Rounded.Analytics,
        unselectedIcon = Icons.Outlined.Analytics,
        iconTextId = R.string.analytics,
        titleTextId = R.string.analytics,
        route = analyticsNavigationRoute
    )
}