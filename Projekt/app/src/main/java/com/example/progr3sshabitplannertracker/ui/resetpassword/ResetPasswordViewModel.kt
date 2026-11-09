package com.example.progr3sshabitplannertracker.ui.resetpassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.data.model.ResetPasswordDto
import com.example.progr3sshabitplannertracker.data.network.RetrofitClient
import com.example.progr3sshabitplannertracker.data.network.bodyOrThrow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val api = RetrofitClient.getInstance(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _message.value = "Kérlek, add meg az e-mail címedet!"
            _isSuccess.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null
            try {
                api.resetPasswordViaEmail(ResetPasswordDto(email = email)).bodyOrThrow("Failed to reset password")
                _isSuccess.value = true
                _message.value = "Az új jelszót elküldtük az e-mail címedre!"
            } catch (e: Exception) {
                _isSuccess.value = false
                _message.value = e.message ?: "Hiba történt a jelszó-visszaállítás során"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
