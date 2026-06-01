package com.example.mapsapp.domain.repository

import com.example.mapsapp.domain.model.Marker

interface MarkerRepository {
    suspend fun saveMarker(marker: Marker, imageBytes: ByteArray?): Result<Unit>
    suspend fun getMarkers(): Result<List<Marker>>
    suspend fun deleteMarker(marker: Marker): Result<Unit>
    suspend fun updateMarker(marker: Marker, imageBytes: ByteArray?): Result<Unit>
}
