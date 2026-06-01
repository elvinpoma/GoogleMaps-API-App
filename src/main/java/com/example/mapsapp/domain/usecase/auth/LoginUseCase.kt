package com.example.mapsapp.domain.usecase.auth

import com.example.mapsapp.data.repository.mapper.AuthRepositoryImpl
import com.example.mapsapp.data.utils.AuthResult

class LoginUseCase {
    private val authRepo = AuthRepositoryImpl()
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val result = authRepo.signIn(email, password)
        if(result is AuthResult.Error){
            var error = ""
            if (result.message.contains("invalid_credentials")) {
                error = "Invalid credentials"
            } else {
                error = "An error has ocurred"
            }
            return Result.failure(IllegalArgumentException(error))
        }
        else{
            return Result.success(Unit)
        }
    }
}
