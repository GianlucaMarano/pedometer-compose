@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.pedometer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedometer.domain.PermissionsUseCase
import com.example.pedometer.feature.easteregg.navigation.EGNavigationRoute
import com.example.pedometer.feature.home.navigation.homeNavigationRoute
import com.example.pedometer.navigation.TopLevelDestination
import com.example.pedometer.ui.AppUiIntent.BottomBarClick
import com.example.pedometer.ui.AppUiIntent.OnBackButtonClick
import com.example.pedometer.ui.AppUiIntent.PermissionGranted
import com.example.pedometer.ui.NavigateIntent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val permissionUseCase: PermissionsUseCase
) : ViewModel() {

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> =
        _appState.mapLatest {
            it.copy(permissionsGranted = permissionUseCase())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AppState())

    /*init {
        viewModelScope.launch {
            _appState.update {
                it.copy(
                    permissionsGranted = permissionUseCase.checkPermissions(),
                )
            }
        }
    }*/


    fun sendEvent(event: AppUiIntent) {
        viewModelScope.launch {
            when (event) {
                is BottomBarClick -> {
                    val destination = if (shouldShowEasterEgg(event.destination.route))
                        EGNavigationRoute
                    else
                        event.destination.route
                    _appState.update {
                        it.copy(
                            currentDestination = destination,
                            navigateTo = Destination(destination),
                            isCurrentDestinationTopLevel = it.bottomBarDestinations.any { it.route == destination },
                            isHomeDestination = destination == homeNavigationRoute
                        )
                    }
                }

                OnBackButtonClick -> _appState.update { state ->
                    state.copy(
                        navigateTo = Back,
                        isCurrentDestinationTopLevel = state.bottomBarDestinations.any { it.route == state.currentDestination },
                        isHomeDestination = state.currentDestination == homeNavigationRoute
                    )
                }

                is PermissionGranted -> _appState.update { state ->
                    state.copy(
                        permissionsGranted = event.granted,
                        isCurrentDestinationTopLevel = state.bottomBarDestinations.any { it.route == state.currentDestination },
                        isHomeDestination = state.currentDestination == homeNavigationRoute
                    )
                }

                is AppUiIntent.PopUpTo ->
                    _appState.update { state ->
                        state.copy(
                            currentDestination = event.route,
                            isCurrentDestinationTopLevel = state.bottomBarDestinations.any { it.route == event.route },
                            isHomeDestination = event.route == homeNavigationRoute
                        )
                    }
            }
        }
    }

    private val clickTimestamps = mutableListOf<Long>()

    private fun shouldShowEasterEgg(route: String): Boolean {
        val currentTime = System.currentTimeMillis()
        if (route == homeNavigationRoute)
            clickTimestamps.add(currentTime)

        clickTimestamps.removeAll { it < currentTime - 5000 }
        return clickTimestamps.size >= 10
    }

}

sealed class NavigateIntent {
    object Back : NavigateIntent()
    data class Destination(val destination: String) : NavigateIntent()
}


sealed class AppUiIntent {
    object OnBackButtonClick : AppUiIntent()
    data class PermissionGranted(val granted: Boolean) : AppUiIntent()
    data class BottomBarClick(val destination: TopLevelDestination) : AppUiIntent()
    data class PopUpTo(val route: String) : AppUiIntent()
}
