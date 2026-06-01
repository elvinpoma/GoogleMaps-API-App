package com.example.mapsapp.data.repository.mapper

import com.example.mapsapp.MyApp
import com.example.mapsapp.data.supabase.models.MarkerEntity
import com.example.mapsapp.domain.model.Marker
import com.example.mapsapp.domain.repository.MarkerRepository

class MarkerRepositoryImpl : MarkerRepository {
    override suspend fun saveMarker(marker: Marker, imageBytes: ByteArray?): Result<Unit> {
        return try {
            var finalImageUrl: String? = null
            
            if (imageBytes != null) {
                finalImageUrl = MyApp.database.uploadImage(imageBytes)
            }

            val entity = MarkerEntity(
                title = marker.title,
                description = marker.description,
                latitude = marker.latitude,
                longitude = marker.longitude,
                imageUrl = finalImageUrl
            )

            MyApp.database.insertMarker(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMarkers(): Result<List<Marker>> {
        return try {
            val entities = MyApp.database.getAllMarkers()
            val markers = entities.map { entity ->
                Marker(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    imageUrl = entity.imageUrl
                )
            }
            Result.success(markers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMarker(marker: Marker): Result<Unit> {
        return try {
            marker.id?.let { id ->
                // Opcional: Borrar la imagen del Storage si existe
                marker.imageUrl?.let { url ->
                    MyApp.database.deleteImage(url)
                }
                MyApp.database.deleteMarker(id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMarker(marker: Marker, imageBytes: ByteArray?): Result<Unit> {
        return try {
            marker.id?.let { id ->
                var finalImageUrl = marker.imageUrl
                
                if (imageBytes != null) {
                    // Si hay nueva imagen, borramos la anterior y subimos la nueva
                    marker.imageUrl?.let { oldUrl ->
                        MyApp.database.deleteImage(oldUrl)
                    }
                    finalImageUrl = MyApp.database.uploadImage(imageBytes)
                }

                val entity = MarkerEntity(
                    title = marker.title,
                    description = marker.description,
                    latitude = marker.latitude,
                    longitude = marker.longitude,
                    imageUrl = finalImageUrl
                )
                MyApp.database.updateMarker(id, entity)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
