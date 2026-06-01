package com.example.mapsapp.ui.screens.map.mapa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.mapsapp.data.permissions.AppPermission
import com.example.mapsapp.data.permissions.PermissionStatus
import com.example.mapsapp.ui.components.PermissionContent
import com.example.mapsapp.ui.components.rememberPermissionManager
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mapsapp.domain.model.Marker as MarkerModel

@Composable
fun MapScreen(refreshTrigger: Boolean = false, navigateToCreateMarker: (Double, Double) -> Unit) {
    val viewModel: MapViewModel = viewModel()
    val permissionManager = rememberPermissionManager(AppPermission.Location)
    val markers by viewModel.markers.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    var selectedMarker by remember { mutableStateOf<MarkerModel?>(null) }
    var quickInfoMarker by remember { mutableStateOf<MarkerModel?>(null) }

    // Recargar cuando el trigger cambie
    LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) viewModel.loadMarkers()
    }

    LaunchedEffect(Unit) {
        viewModel.loadMarkers()
    }
    
    LaunchedEffect(permissionManager.status) {
        if (permissionManager.status == PermissionStatus.Unknown) {
            permissionManager.requestPermissions()
        }
        viewModel.onPermissionResult(permissionManager.status)
        if (permissionManager.status == PermissionStatus.Granted) {
            viewModel.loadMarkers()
        }
    }

    if (viewModel.uiState.value == MapPermissionState.NavigateToMap) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(41.453, 2.186), 12f)
                },
                onMapLongClick = { navigateToCreateMarker(it.latitude, it.longitude) },
                onMapClick = { quickInfoMarker = null } // Cerrar tarjeta al tocar el mapa
            ) {
                markers.forEach { marker ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(marker.latitude, marker.longitude)),
                        onClick = {
                            quickInfoMarker = marker
                            false // Dejar que la cámara se centre
                        }
                    )
                }
            }
            
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Tarjeta de información rápida en la parte inferior
            quickInfoMarker?.let { marker ->
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                ) {
                    MarkerQuickCard(
                        marker = marker,
                        onClick = { 
                            selectedMarker = marker
                            quickInfoMarker = null 
                        }
                    )
                }
            }

            // Detalle del marcador en pantalla completa
            selectedMarker?.let { marker ->
                MarkerDetailDialog(
                    marker = marker,
                    onDismiss = { selectedMarker = null }
                )
            }
        }
    } else {
        PermissionContent(permissionManager.status, permissionManager.requestPermissions)
    }
}

@Composable
fun MarkerDetailDialog(marker: MarkerModel, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Imagen de fondo (o centrada)
                if (!marker.imageUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = marker.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        loading = {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            tint = Color.White.copy(alpha = 0.2f)
                        )
                    }
                }

                // Overlay de información en la parte inferior
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .navigationBarsPadding()
                    ) {
                        Text(
                            text = marker.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (marker.description.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = marker.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "📍 ${"%.4f".format(marker.latitude)}, ${"%.4f".format(marker.longitude)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Botón cerrar
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun MarkerQuickCard(marker: MarkerModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniatura con cargador
            Surface(
                modifier = Modifier.size(76.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                if (!marker.imageUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = marker.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            }
                        }
                    )
                } else {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = marker.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "Toca para ver descripción y fotos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(20.dp).clickable { /* El onMapClick ya lo cierra */ },
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}
