package com.example.mapsapp.domain.usecase.marker

import com.example.mapsapp.data.repository.mapper.MarkerRepositoryImpl
import com.example.mapsapp.domain.model.Marker
import com.example.mapsapp.domain.repository.MarkerRepository

class DeleteMarkerUseCase(private val repository: MarkerRepository = MarkerRepositoryImpl()) {
    suspend operator fun invoke(marker: Marker): Result<Unit> {
        return repository.deleteMarker(marker)
    }
}
