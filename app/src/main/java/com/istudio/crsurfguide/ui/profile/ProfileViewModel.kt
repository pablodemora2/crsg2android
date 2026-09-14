package com.istudio.crsurfguide.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val uid = authRepository.getCurrentUserId()
        if (uid == null) {
            _profileState.value = ProfileState.Error("User not authenticated")
            return
        }

        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            userRepository.getUserProfile(uid).onSuccess {
                _profileState.value = ProfileState.Success(it)
            }.onFailure {
                _profileState.value = ProfileState.Error(it.message ?: "Failed to load profile")
            }
        }
    }

    fun updateProfile(name: String, surfLevel: String, favoriteSpot: String) {
        val currentState = _profileState.value
        if (currentState is ProfileState.Success) {
            val updatedProfile = currentState.profile.copy(
                name = name,
                surfLevel = surfLevel,
                favoriteSpot = favoriteSpot
            )

            viewModelScope.launch {
                _isUpdating.value = true
                userRepository.updateUserProfile(updatedProfile).onSuccess {
                    _profileState.value = ProfileState.Success(updatedProfile)
                }
                _isUpdating.value = false
            }
        }
    }

    fun uploadImage(uri: Uri) {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isUpdating.value = true
            userRepository.uploadProfileImage(uid, uri).onSuccess { newUrl ->
                val currentState = _profileState.value
                if (currentState is ProfileState.Success) {
                    _profileState.value = ProfileState.Success(
                        currentState.profile.copy(profileImageUrl = newUrl)
                    )
                }
            }
            _isUpdating.value = false
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: UserProfile) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
