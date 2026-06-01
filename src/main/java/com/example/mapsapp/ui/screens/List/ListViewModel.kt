package com.example.mapsapp.ui.screens.List

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.domain.model.Marker
import com.example.mapsapp.domain.usecase.marker.DeleteMarkerUseCase
import com.example.mapsapp.domain.usecase.marker.GetMarkersUseCase
import com.example.mapsapp.domain.usecase.marker.UpdateMarkerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val getMarkersUseCase = GetMarkersUseCase()
    private val deleteMarkerUseCase = DeleteMarkerUseCase()
    private val updateMarkerUseCase = UpdateMarkerUseCase()

    private val _markers = MutableStateFlow<List<Marker>>(emptyList())
    val markers: StateFlow<List<Marker>> = _markers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadMarkers()
    }

    fun loadMarkers() {
        viewModelScope.launch {
            _isLoading.value = true
            getMarkersUseCase().onSuccess {
                _markers.value = it
                _error.value = null
            }.onFailure {
                _error.value = it.message ?: "Error al cargar los marcadores"
            }
            _isLoading.value = false
        }
    }

    fun deleteMarker(marker: Marker) {
        viewModelScope.launch {
            deleteMarkerUseCase(marker).onSuccess {
                loadMarkers()
            }.onFailure {
                _error.value = it.message ?: "Error al eliminar"
            }
        }
    }

    fun updateMarker(marker: Marker, imageBytes: ByteArray?, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            updateMarkerUseCase(marker, imageBytes).onSuccess {
                loadMarkers()
                onComplete()
            }.onFailure {
                _error.value = it.message ?: "Error al actualizar"
            }
            _isLoading.value = false
        }
    }
}
