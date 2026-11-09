package com.example.progr3sshabitplannertracker.ui.profile

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.data.model.ProfileResponseDto
import com.example.progr3sshabitplannertracker.data.repository.UserRepository
import com.example.progr3sshabitplannertracker.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    private val sessionManager = SessionManager(application)

    private val _profile = MutableLiveData<ProfileResponseDto?>()
    val profile: LiveData<ProfileResponseDto?> = _profile

    private val _isLogoutSuccess = MutableLiveData<Boolean>()
    val isLogoutSuccess: LiveData<Boolean> = _isLogoutSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadProfile() {
        viewModelScope.launch {
            val result = userRepository.getProfile()
            result.onSuccess { data ->
                _profile.value = data
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    fun uploadProfileImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val file = File(context.cacheDir, "profile_img.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("profileImage", file.name, requestFile)

                val result = userRepository.uploadProfileImage(body)
                result.onSuccess { data ->
                    _profile.value = data
                }.onFailure { error ->
                    _errorMessage.value = error.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to upload image"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            sessionManager.clearAuthToken()
            _isLogoutSuccess.value = true
        }
    }
}
