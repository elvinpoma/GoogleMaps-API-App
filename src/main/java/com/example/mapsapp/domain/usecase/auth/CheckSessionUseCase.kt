package com.example.mapsapp.domain.usecase.auth

import com.example.mapsapp.data.repository.mapper.AuthRepositoryImpl

class CheckSessionUseCase {
    private val authRepo = AuthRepositoryImpl()

    suspend operator fun invoke(): Boolean {
        return authRepo.checkExistingSession()
    }
}
