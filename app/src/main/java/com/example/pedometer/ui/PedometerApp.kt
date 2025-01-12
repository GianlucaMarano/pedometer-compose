package com.example.pedometer.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.permission.HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_HISTORY
import androidx.health.connect.client.records.StepsRecord
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pedometer.feature.home.ui.components.background.movingStripesBackground
import com.example.pedometer.navigation.NavigationHandler
import com.example.pedometer.navigation.PedometerNavHost
import com.example.pedometer.navigation.TopLevelDestination
import com.example.pedometer.ui.AppUiIntent.BottomBarClick
import com.example.pedometer.ui.AppUiIntent.OnBackButtonClick
import com.example.pedometer.ui.AppUiIntent.PermissionGranted
import com.example.pedometer.ui.component.PedometerAppBar
import com.example.pedometer.ui.component.PedometerBottomBar
import com.example.pedometer.ui.component.RequestPermissions
import com.example.pedometer.ui.theme.Purple40

@Composable
fun PedometerApp(
    viewModel: AppViewModel = hiltViewModel<AppViewModel>(),
) {
    val appState by viewModel.appState.collectAsStateWithLifecycle()

    if (appState.permissionsGranted) {
        Content(
            currentDestination = appState.bottomBarDestinations.find { appState.currentDestination == it.route }
                ?: TopLevelDestination.HOME,
            destinations = appState.bottomBarDestinations,
            isTopLevelDestination = appState.isCurrentDestinationTopLevel,
            isHomeDestination = appState.isHomeDestination,
            navigateTo = appState.navigateTo,
            onEvent = viewModel::sendEvent,
        )
    } else {
        Surface {
            RequestPermissions(
                requiredPermissions = setOf(
                    PERMISSION_READ_HEALTH_DATA_HISTORY,
                    HealthPermission.getReadPermission(StepsRecord::class)
                )
            ) { granted ->
                viewModel.sendEvent(PermissionGranted(granted))
            }
        }
    }
}

@Composable
fun Content(
    navController: NavHostController = rememberNavController(),
    currentDestination: TopLevelDestination,
    destinations: List<TopLevelDestination>,
    isHomeDestination: Boolean,
    isTopLevelDestination: Boolean,
    navigateTo: NavigateIntent? = null,
    onEvent: (AppUiIntent) -> Unit,
) {
    NavigationHandler(
        navController = navController,
        navigationIntent = navigateTo,
        onEvent = { onEvent(it) },
    )
    Scaffold(
        topBar = {
            if (!isTopLevelDestination) {
                PedometerAppBar(
                    showBackButton = true,
                    onClickBackButton = {
                        onEvent(OnBackButtonClick)
                    }

                )
            }
        },
        bottomBar = {
            if (isTopLevelDestination) {
                PedometerBottomBar(
                    destinations = destinations,
                    onNavigateToDestination = { onEvent(BottomBarClick(it)) },
                    currentDestination = currentDestination,
                    modifier = Modifier.testTag("PedometerBottomBar"),
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        BackHandler(enabled = !isHomeDestination) { onEvent(OnBackButtonClick) }
        PedometerNavHost(
            navController = navController,
            modifier = Modifier
                .movingStripesBackground(
                    stripeColor = Purple40,
                    backgroundColor = MaterialTheme.colorScheme.background,
                )
                .padding(innerPadding)
        )
    }
}