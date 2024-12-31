package com.example.pedometer.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.example.pedometer.feature.analytics.navigation.navigateAnalytics
import com.example.pedometer.feature.easteregg.navigation.EGNavigationRoute
import com.example.pedometer.feature.easteregg.navigation.navigateEG
import com.example.pedometer.feature.home.navigation.navigateHome
import com.example.pedometer.ui.AppUiIntent
import com.example.pedometer.ui.NavigateIntent

@Composable
fun NavigationHandler(
    navController: NavHostController,
    navigationIntent: NavigateIntent?,
    onEvent: (AppUiIntent) -> Unit,
) {
    LaunchedEffect(navigationIntent) {
        navigationIntent?.let { intent ->
            when (intent) {
                NavigateIntent.Back -> {
                    navController.navigateUp()
                    navController.currentDestination?.route?.let { newRoute ->
                        onEvent(AppUiIntent.PopUpTo(newRoute))
                    }
                }

                is NavigateIntent.Destination -> {
                    navController.NavigateToTopLevelDestination(
                        intent.destination
                    )
                }

            }
        }
    }
}

fun NavHostController?.NavigateToTopLevelDestination(route: String) {
    val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        this@NavigateToTopLevelDestination?.graph?.findStartDestination()?.id?.let { startDestinationId ->
            popUpTo(startDestinationId) {
                saveState = true
            }
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

    when (route) {
        TopLevelDestination.HOME.route -> this@NavigateToTopLevelDestination?.navigateHome(
            topLevelNavOptions
        )

        TopLevelDestination.ANALYTICS.route -> this@NavigateToTopLevelDestination?.navigateAnalytics(
            topLevelNavOptions
        )

        EGNavigationRoute -> this@NavigateToTopLevelDestination?.navigateEG(navOptions = navOptions {})

        else -> Unit
    }
}