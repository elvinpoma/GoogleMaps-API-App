package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.mapsapp.ui.screens.map.mapa.MapScreen
import com.example.mapsapp.ui.screens.map.marker.permisions.AddMarkerScreen

@Composable
fun MapScreenNavigation() {
    val mapScreen2BackStack = rememberNavBackStack(MapScreenRoute.Mapscreen1)
    
    // Forzamos el refresco del mapa cuando el backstack vuelve a tener solo 1 elemento
    val showRefresh = mapScreen2BackStack.size == 1

    NavDisplay(
        backStack = mapScreen2BackStack,
        onBack = { mapScreen2BackStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MapScreenRoute.Mapscreen1> {
                MapScreen(
                    refreshTrigger = showRefresh, // Nuevo parámetro
                    navigateToCreateMarker = { lat, long ->
                        mapScreen2BackStack.add(MapScreenRoute.Mapscreen2(lat, long))
                    }
                )
            }
            entry<MapScreenRoute.Mapscreen2> { key ->
                AddMarkerScreen(key.lat, key.long) { mapScreen2BackStack.removeLastOrNull()!! }
            }
        }
    )
}
