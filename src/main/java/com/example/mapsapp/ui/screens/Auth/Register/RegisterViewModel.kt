package com.example.mapsapp.ui.screens.Auth.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(): ViewModel() {
    private val registerUseCase = RegisterUseCase()

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

    fun signUp() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            registerUseCase(_email.value, _password.value)
                .onSuccess {
                    withContext(Dispatchers.Main){
                        _isLoggedIn.value = true
                        _isLoading.value = false
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

}
