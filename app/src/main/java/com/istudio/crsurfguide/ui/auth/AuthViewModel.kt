package com.istudio.crsurfguide.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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

    fun loginWithFakeUser(name: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("AuthViewModel", "Intentando login simulado para: $name")
            try {
                val formattedName = name.replace(" ", "+")
                val avatarUrl = "https://ui-avatars.com/api/?name=$formattedName&background=0D8ABC&color=fff"
                val email = "${name.lowercase().replace(" ", "")}@example.com"
                
                // Creación o selección del usuario mockeado en la BD local
                val fakeProfile = userRepository.getOrCreateFakeUser(name, email, avatarUrl)
                
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("AuthViewModel", "Fake User creado/recuperado: ${fakeProfile.uid}")
                
                // Forzamos un login simulado inyectando temporalmente un token/ID alternativo si es necesario
                // Como pasamos a SurfListScreen mediante AuthState.Success, la app cargará los datos de este UID
                _authState.value = AuthState.SuccessFake(fakeProfile.uid)
            } catch (e: Exception) {
                com.istudio.crsurfguide.ui.debug.LogBuffer.e("AuthViewModel", "Error en login fake", e)
                _authState.value = AuthState.Error(e.message ?: "Error simulando login")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class SuccessFake(val fakeUid: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
