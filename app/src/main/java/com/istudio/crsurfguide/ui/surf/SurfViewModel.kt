package com.istudio.crsurfguide.ui.surf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.model.SurfWeather
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.SurfRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import com.istudio.crsurfguide.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurfViewModel @Inject constructor(
    private val surfRepository: SurfRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _rawSpots = MutableStateFlow<List<SurfSpot>>(emptyList())
    
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedSpot = MutableStateFlow<SurfSpot?>(null)
    val selectedSpot: StateFlow<SurfSpot?> = _selectedSpot.asStateFlow()

    private val _surfWeather = MutableStateFlow<SurfWeather?>(null)
    val surfWeather: StateFlow<SurfWeather?> = _surfWeather.asStateFlow()

    // Favoritos locales reactivos
    val favoriteIds: StateFlow<List<String>> = userRepository.getLocalFavoriteIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spots: StateFlow<List<SurfSpot>> = combine(_rawSpots, favoriteIds, _showOnlyFavorites) { spots, favs, onlyFavs ->
        if (onlyFavs) {
            spots.filter { spot -> favs.contains(spot.id) }
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

    fun loadSpotById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _surfWeather.value = null // Reset weather
            surfRepository.getSpotById(id).onSuccess { spot ->
                _selectedSpot.value = spot
                fetchWeather(spot.latitude, spot.longitude)
            }.onFailure {
                // Handle error
            }
            _isLoading.value = false
        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherRepository.getSurfWeather(lat, lon).onSuccess {
                _surfWeather.value = it
            }
        }
    }

    fun toggleFavorite(spotId: String) {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            userRepository.toggleFavoriteSpot(uid, spotId)
            // No necesitamos refrescar el perfil completo para los IDs, el flow local se encarga
        }
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }
}
