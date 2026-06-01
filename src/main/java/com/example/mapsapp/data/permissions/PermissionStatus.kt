package com.example.mapsapp.data.permissions

sealed class PermissionStatus {
    data object Unknown : PermissionStatus()
    data object Granted : PermissionStatus()
    data object Denied : PermissionStatus()
    data object PermanentlyDenied : PermissionStatus()
}
