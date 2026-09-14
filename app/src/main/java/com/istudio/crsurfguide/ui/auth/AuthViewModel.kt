package com.istudio.crsurfguide.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signIn(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signInWithEmail(email, pass).onSuccess {
                _authState.value = AuthState.Success
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signUpWithEmail(email, pass).onSuccess {
                _authState.value = AuthState.Success
            }.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Registration failed")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
