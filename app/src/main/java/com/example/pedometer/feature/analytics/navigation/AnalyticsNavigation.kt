package com.example.pedometer.feature.analytics.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.pedometer.feature.analytics.ui.AnalyticsScreen
import com.example.pedometer.feature.analytics.ui.AnalyticsViewModel
import androidx.compose.runtime.getValue

const val analyticsNavigationRoute = "analytics_route"

fun NavController.navigateAnalytics(navOptions: NavOptions? = null) {
    this.navigate(analyticsNavigationRoute, navOptions)
}

fun NavGraphBuilder.analyticsScreen() {
    composable(route = analyticsNavigationRoute)
    {
        val viewModel = hiltViewModel<AnalyticsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        AnalyticsScreen(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}