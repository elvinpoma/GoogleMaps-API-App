package com.example.mapsapp.data.repository.mapper

import com.example.mapsapp.MyApp
import com.example.mapsapp.data.utils.AuthResult
import com.example.mapsapp.domain.repository.AuthRepository

class AuthRepositoryImpl: AuthRepository {
    private val client = MyApp.Companion.database
    override suspend fun checkExistingSession(): Boolean {
        return client.isLoggedIn()
    }
    override suspend fun signIn(email: String, password: String): AuthResult {
        return client.login(email, password)
    }
    override suspend fun signUp(email: String, password: String): AuthResult {
        return client.register(email, password)
    }
    override suspend fun logout(): AuthResult {
        return client.logout()
    }
}