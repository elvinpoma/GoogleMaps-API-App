package com.example.mapsapp.domain.usecase.marker

import com.example.mapsapp.data.repository.mapper.MarkerRepositoryImpl
import com.example.mapsapp.domain.model.Marker

class GetMarkersUseCase {
    private val repository = MarkerRepositoryImpl()
    suspend operator fun invoke(): Result<List<Marker>> {
        return repository.getMarkers()
    }
}
