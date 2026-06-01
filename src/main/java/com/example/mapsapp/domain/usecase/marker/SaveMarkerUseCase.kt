package com.example.mapsapp.domain.usecase.marker

import com.example.mapsapp.data.repository.mapper.MarkerRepositoryImpl
import com.example.mapsapp.domain.model.Marker

class SaveMarkerUseCase {
    private val repository = MarkerRepositoryImpl()
    suspend operator fun invoke(marker: Marker, imageBytes: ByteArray?): Result<Unit> {
        return repository.saveMarker(marker, imageBytes)
    }
}
