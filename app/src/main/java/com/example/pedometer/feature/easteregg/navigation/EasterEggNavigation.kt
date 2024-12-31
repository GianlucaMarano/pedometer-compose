package com.example.pedometer.feature.easteregg.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pedometer.feature.easteregg.ui.EasterEggScreen

const val EGNavigationRoute = "eg_route"

fun NavController.navigateEG(navOptions: NavOptions? = null) {
    this.navigate(EGNavigationRoute, navOptions)
}

fun NavGraphBuilder.EGScreen() {
    composable(route = EGNavigationRoute,)
    {
        EasterEggScreen()
    }
}