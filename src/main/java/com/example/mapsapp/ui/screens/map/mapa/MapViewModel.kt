package com.example.mapsapp.ui.screens.map.mapa

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.data.permissions.PermissionStatus
import com.example.mapsapp.domain.model.Marker
import com.example.mapsapp.domain.usecase.marker.GetMarkersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val getMarkersUseCase = GetMarkersUseCase()

    private val _uiState = mutableStateOf<MapPermissionState>(MapPermissionState.Requesting)
    val uiState: State<MapPermissionState> = _uiState

    private val _markers = MutableStateFlow<List<Marker>>(emptyList())
    val markers: StateFlow<List<Marker>> = _markers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onPermissionResult(status: PermissionStatus) {
        _uiState.value = when (status) {
            PermissionStatus.Granted -> {
                loadMarkers()
                MapPermissionState.NavigateToMap
            }

            PermissionStatus.Denied -> MapPermissionState.ShowDenied
            PermissionStatus.PermanentlyDenied -> MapPermissionState.ShowPermanentlyDenied
            PermissionStatus.Unknown -> MapPermissionState.Requesting
        }
    }

    fun loadMarkers() {
        viewModelScope.launch {
            _isLoading.value = true
            getMarkersUseCase().onSuccess {
                _markers.value = it
                _isLoading.value = false
            }
                .onFailure {
                    val error = it.message
                    Log.d("NOE", error!!)
                    _isLoading.value = false
                }
        }
    }
}
