package com.example.progr3sshabitplannertracker.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    fun checkAuthStatus() {
        viewModelScope.launch {
            delay(1000)
            val token = sessionManager.fetchAuthToken()
            _isLoggedIn.value = !token.isNullOrBlank()
        }
    }
}
