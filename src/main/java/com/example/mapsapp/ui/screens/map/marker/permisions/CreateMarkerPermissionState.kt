package com.example.mapsapp.ui.screens.map.marker.permisions

sealed class CreateMarkerPermissionState {
    object Requesting : CreateMarkerPermissionState()
    object ShowDenied : CreateMarkerPermissionState()
    object ShowPermanentlyDenied : CreateMarkerPermissionState()
    object NavigateToForm : CreateMarkerPermissionState()

}