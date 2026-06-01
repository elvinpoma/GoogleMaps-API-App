package com.example.mapsapp.domain.usecase.auth

import com.example.mapsapp.data.repository.mapper.AuthRepositoryImpl
import com.example.mapsapp.data.utils.AuthResult

class RegisterUseCase {
    private val authRepo = AuthRepositoryImpl()
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val result = authRepo.signUp(email, password)
        if (result is AuthResult.Error){
            return Result.failure(IllegalArgumentException(result.message))
        }
        else {
            return Result.success(Unit)
        }
    }
}
