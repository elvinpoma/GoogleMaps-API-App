package com.example.mapsapp.ui.screens.map.marker.permisions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.mapsapp.data.permissions.AppPermission
import com.example.mapsapp.data.permissions.PermissionStatus
import com.example.mapsapp.ui.components.PermissionContent
import com.example.mapsapp.ui.components.rememberPermissionManager
import com.example.mapsapp.ui.screens.map.marker.form.Formulario2


@Composable
fun AddMarkerScreen(lat: Double, long: Double, onBack: () -> NavKey) {
    val viewModel: CreateMarkerViewModel = viewModel()
    val permissionManager = rememberPermissionManager(AppPermission.CameraAndAudio)
    val uiState by viewModel.uiState
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        viewModel.onPermissionResult(permissionManager.status)
    }
    when (uiState) {

        CreateMarkerPermissionState.NavigateToForm -> {
            Formulario2(
                viewModel = viewModel,
                lat = lat,
                long = long,
                isSaving = isSaving,
                onSave = { onBack() },
                onBack = { 
                    viewModel.resetForm()
                    onBack() 
                }
            )
        }

        CreateMarkerPermissionState.ShowDenied -> PermissionContent(
            PermissionStatus.Denied,
            permissionManager.requestPermissions
        )

        CreateMarkerPermissionState.ShowPermanentlyDenied -> PermissionContent(
            PermissionStatus.PermanentlyDenied,
            {})

        CreateMarkerPermissionState.Requesting -> PermissionContent(
            PermissionStatus.Unknown,
            permissionManager.requestPermissions
        )
    }
}
