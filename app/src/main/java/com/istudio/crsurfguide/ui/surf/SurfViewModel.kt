package com.istudio.crsurfguide.ui.surf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.SurfRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurfViewModel @Inject constructor(
    private val surfRepository: SurfRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _rawSpots = MutableStateFlow<List<SurfSpot>>(emptyList())
    
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val spots: StateFlow<List<SurfSpot>> = combine(_rawSpots, _userProfile, _showOnlyFavorites) { spots, profile, onlyFavs ->
        if (onlyFavs) {
            spots.filter { spot -> profile?.favoriteSurfSpotIds?.contains(spot.id) == true }
        } else {
            spots
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadSpots()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            userRepository.getUserProfile(uid).onSuccess {
                _userProfile.value = it
            }
        }
    }

    fun loadSpots() {
        viewModelScope.launch {
            _isLoading.value = true
            surfRepository.getSurfSpots().collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _rawSpots.value = it
                }
            }
        }
    }

    fun toggleFavorite(spotId: String) {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            userRepository.toggleFavoriteSpot(uid, spotId).onSuccess {
                // Refresh profile to update UI state
                loadUserProfile()
            }
        }
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }
}
