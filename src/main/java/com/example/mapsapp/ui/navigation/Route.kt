package com.example.mapsapp.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Route : NavKey {

    @Serializable
    data object InitialChecking: Route()

    @Serializable
    data object Login: Route()
    @Serializable
    data object Register: Route()

    @Serializable
    data object MapScreen : Route()

    @Serializable
    data object ListScreen : Route()

    @Serializable
    data object LogOut: Route()

}

sealed class MapScreenRoute : NavKey {
    @Serializable
    data object Mapscreen1 : MapScreenRoute()

    @Serializable
    data class Mapscreen2(
        val lat: Double,
        val long: Double
    ) : MapScreenRoute() {

    }


}