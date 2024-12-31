package com.example.pedometer.feature.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pedometer.feature.home.ui.HomeViewModel
import com.example.pedometer.feature.home.ui.StepsScreen

const val homeNavigationRoute = "home_route"

fun NavController.navigateHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen() {
    composable(
        route = homeNavigationRoute,
    )
    {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        StepsScreen(
            uiState = state,
            onIntent = viewModel::handleIntent,
            modifier = Modifier.fillMaxSize()
        )
    }
}