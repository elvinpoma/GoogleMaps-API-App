package com.example.mapsapp.ui.screens.Auth.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.domain.usecase.auth.CheckSessionUseCase
import com.example.mapsapp.domain.usecase.auth.LoginUseCase
import com.example.mapsapp.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel() : ViewModel() {
    val checkSessionUseCase = CheckSessionUseCase()
    val loginUseCase = LoginUseCase()
    val logoutUseCase = LogoutUseCase()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn
    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    //Metodos
    fun editEmail(value: String) {
        _email.value = value
    }

    fun editPassword(value: String) {
        _password.value = value
    }

    fun errorMessageShowed() {
        _showError.value = false
        _errorMessage.value = ""
    }

    fun resetLoginState() {
        _isLoggedIn.value = false
        _showError.value = false
    }

    fun checkExistingSession() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val hasSession = checkSessionUseCase()
            withContext(Dispatchers.Main) {
                _isLoggedIn.value = hasSession
                _isLoading.value = false
            }
        }
    }

    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            loginUseCase(_email.value, _password.value)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        _isLoggedIn.value = true
                        _isLoading.value = false
                    }
                }
                .onFailure { exception ->
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = exception.message ?: "Error"
                        _showError.value = true
                        _isLoading.value = false
                    }
                }
        }
    }

    fun logout(onComplete: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            logoutUseCase()
                .onSuccess {
                    withContext(Dispatchers.Main){
                        clearFields() // Limpiar campos por seguridad
                        _isLoggedIn.value = false
                        _isLoading.value = false
                        onComplete() 
                    }
                }
                .onFailure { exception ->
                    withContext(Dispatchers.Main){
                        _errorMessage.value = exception.message ?: "Error"
                        _showError.value = true
                        _isLoading.value = false
                    }
                }
        }
    }

    private fun clearFields() {
        _email.value = ""
        _password.value = ""
        _errorMessage.value = ""
    }
}
