package com.example.mapsapp.domain.model

data class Marker(
    val id: String? = null,
    val userId: String? = null,
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null
)
