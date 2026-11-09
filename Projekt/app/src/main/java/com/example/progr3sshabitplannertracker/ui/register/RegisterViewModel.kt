package com.example.progr3sshabitplannertracker.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
        
    var passwordsDoNotMatch by mutableStateOf(false)
        private set

    fun updateUsername(input: String) { username = input }
    fun updateEmail(input: String) { email = input }
    fun updatePassword(input: String) { 
        password = input
        checkPasswordMatch()
    }
    fun updateConfirmPassword(input: String) { 
        confirmPassword = input
        checkPasswordMatch()
    }

    private fun checkPasswordMatch() {
        passwordsDoNotMatch = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword
    }

    fun register(onSuccess: () -> Unit) {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Kérlek, töltsd ki az összes mezőt!"
            return
        }
        
        if (password != confirmPassword) {
            passwordsDoNotMatch = true
            errorMessage = "A jelszavak nem egyeznek!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Mock POST /auth/local/signup
            delay(1500) 

            isLoading = false
            onSuccess() // Assuming successful registration immediately redirects to Home
        }
    }
    
    fun registerWithGoogle(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Mock POST /auth/google
            delay(1500) 

            isLoading = false
            onSuccess() 
        }
    }
}
