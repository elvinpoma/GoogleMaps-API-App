package com.example.mapsapp.ui.screens.map.marker.permisions

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.data.permissions.PermissionStatus
import com.example.mapsapp.domain.model.Marker
import com.example.mapsapp.domain.usecase.marker.SaveMarkerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateMarkerViewModel : ViewModel() {
    private val saveMarkerUseCase = SaveMarkerUseCase()

    private val _titulo = MutableStateFlow("")
    val titulo = _titulo.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion = _descripcion.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    private var _imageBytes: ByteArray? = null

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val uiState = mutableStateOf<CreateMarkerPermissionState>(CreateMarkerPermissionState.Requesting)

    fun onTituloChange(nuevo: String) { _titulo.value = nuevo }
    fun onDescripcionChange(nuevo: String) { _descripcion.value = nuevo }
    
    fun onImageSelected(uri: Uri?, bytes: ByteArray?) {
        _imageUri.value = uri
        _imageBytes = bytes
    }

    fun onPermissionResult(status: PermissionStatus) {
        uiState.value = when (status) {
            PermissionStatus.Granted -> CreateMarkerPermissionState.NavigateToForm
            PermissionStatus.Denied -> CreateMarkerPermissionState.ShowDenied
            PermissionStatus.PermanentlyDenied -> CreateMarkerPermissionState.ShowPermanentlyDenied
            else -> CreateMarkerPermissionState.Requesting
        }
    }

    fun saveMarker(lat: Double, long: Double, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            val marker = Marker(
                title = _titulo.value,
                description = _descripcion.value,
                latitude = lat,
                longitude = long
            )
            
            saveMarkerUseCase(marker, _imageBytes).onSuccess {
                resetForm() // Limpiar después de guardar
                onComplete()
            }.onFailure {
                _error.value = it.message ?: "Error al guardar el marcador"
            }
            _isSaving.value = false
        }
    }

    fun resetForm() {
        _titulo.value = ""
        _descripcion.value = ""
        _imageUri.value = null
        _imageBytes = null
        _error.value = null
    }
}
