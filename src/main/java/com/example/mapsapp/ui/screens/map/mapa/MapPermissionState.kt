package com.example.mapsapp.ui.screens.map.mapa

sealed class MapPermissionState {
    object Requesting : MapPermissionState()
    object ShowDenied : MapPermissionState()
    object ShowPermanentlyDenied : MapPermissionState()
    object NavigateToMap : MapPermissionState()
}
