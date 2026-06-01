package com.example.mapsapp.domain.repository

import com.example.mapsapp.data.utils.AuthResult

interface AuthRepository {
    suspend fun checkExistingSession(): Boolean

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(email: String, password: String): AuthResult

    suspend fun logout(): AuthResult
}
