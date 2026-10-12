package com.example.progr3sshabitplannertracker.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    // A kódod többi része tökéletes, maradhat úgy, ahogy megírtad!
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Függvények a bemenet frissítésére
    fun updateEmail(input: String) { email = input }
    fun updatePassword(input: String) { password = input }
    fun clearError() { errorMessage = null }

    // Bejelentkezés indítása
    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Kérlek, töltsd ki az összes mezőt!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // IDE JÖN MAJD A VALÓDI API HÍVÁS (Retrofit)
            delay(1500) // Szimulálunk egy hálózati kérést (1.5 másodperc)

            if (email == "test@test.com" && password == "password") { // Kamu ellenőrzés
                isLoading = false
                onSuccess()
            } else {
                isLoading = false
                errorMessage = "Hibás e-mail vagy jelszó!"
            }
        }
    }

    fun loginWithGoogle(onSuccess: () -> Unit) {
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