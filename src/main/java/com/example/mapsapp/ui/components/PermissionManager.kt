package com.example.mapsapp.ui.components

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mapsapp.data.permissions.AppPermission
import com.example.mapsapp.data.permissions.PermissionStatus

data class PermissionStateManager(
    val status: PermissionStatus,
    val requestPermissions: () -> Unit
)
@Composable
fun rememberPermissionManager(permission: AppPermission): PermissionStateManager {
    val context = LocalContext.current
    val activity = context as? Activity
    var status by remember {mutableStateOf<PermissionStatus>(PermissionStatus.Unknown)}
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { results ->
        val allGranted = results.values.all { it }
        status = when {
            allGranted -> PermissionStatus.Granted
            activity != null && permission.permissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity,it)
            } -> PermissionStatus.Denied
            else -> PermissionStatus.PermanentlyDenied
        }
    }
    LaunchedEffect(Unit) {
        val allGranted = permission.permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        status = if (allGranted) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Unknown
        }
    }
    return PermissionStateManager(
        status = status,
        requestPermissions = { launcher.launch(permission.permissions.toTypedArray()) }
    )
}
