package com.example.progr3sshabitplannertracker.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.data.model.AuthResponseDto
import com.example.progr3sshabitplannertracker.data.repository.AuthRepository
import com.example.progr3sshabitplannertracker.utils.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val sessionManager = SessionManager(application)

    private val _authResult = MutableLiveData<Result<AuthResponseDto>>()
    val authResult: LiveData<Result<AuthResponseDto>> = _authResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(username, email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    private fun handleResponse(response: Response<AuthResponseDto>) {
        if (response.isSuccessful && response.body() != null) {
            val body = response.body()!!
            body.tokens?.accessToken?.let { token ->
                sessionManager.saveAuthToken(token)
            }
            _authResult.postValue(Result.success(body))
        } else {
            _authResult.postValue(Result.failure(Exception("Auth failed: ${response.code()}")))
        }
    }
}
