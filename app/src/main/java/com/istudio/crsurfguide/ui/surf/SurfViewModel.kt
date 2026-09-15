package com.istudio.crsurfguide.ui.surf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.model.SurfWeather
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.model.SpotReport
import com.istudio.crsurfguide.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri

@HiltViewModel
class SurfViewModel @Inject constructor(
    private val surfRepository: SurfRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val weatherRepository: WeatherRepository,
    private val reportRepository: SurfReportRepository,
    private val seedFakeUserDataUseCase: com.istudio.crsurfguide.domain.usecase.SeedFakeUserDataUseCase
) : ViewModel() {

    companion object {
        var forcedFakeUid: String? = null
    }

    private val _rawSpots = MutableStateFlow<List<SurfSpot>>(emptyList())
    
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _selectedSpot = MutableStateFlow<SurfSpot?>(null)
    val selectedSpot: StateFlow<SurfSpot?> = _selectedSpot.asStateFlow()

    private val _surfWeather = MutableStateFlow<SurfWeather?>(null)
    val surfWeather: StateFlow<SurfWeather?> = _surfWeather.asStateFlow()

    private val _spotReports = MutableStateFlow<List<SpotReport>>(emptyList())
    val spotReports: StateFlow<List<SpotReport>> = _spotReports.asStateFlow()

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

    fun loadUserProfile() {
        val uid = forcedFakeUid ?: authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Cargando perfil para UID: $uid")
            // Aseguramos que si es un usuario fake (QA), exista en el repositorio local
            if (uid.startsWith("fake_")) {
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Detectado Fake User, garantizando persistencia local...")
                userRepository.getOrCreateFakeUser(uid, "", "")
                // Lanzar el seeding de datos si es necesario
                seedFakeUserDataUseCase(uid)
            }
            userRepository.getUserProfile(uid).onSuccess {
                _userProfile.value = it
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Perfil cargado con éxito: ${it.name}")
            }
        }
    }

    fun loadSpots() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Iniciando carga de spots desde repositorio...")
            surfRepository.getSurfSpots().collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _rawSpots.value = it
                    com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Cargados ${it.size} spots exitosamente.")
                }.onFailure {
                    _errorMessage.value = "Error al cargar playas: ${it.localizedMessage}"
                    com.istudio.crsurfguide.ui.debug.LogBuffer.e("SurfViewModel", "Fallo en carga de spots", it)
                }
            }
        }
    }

    fun loadSpotById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _surfWeather.value = null
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Cargando detalle de spot ID: $id")
            surfRepository.getSpotById(id).onSuccess { spot ->
                _selectedSpot.value = spot
                fetchWeather(spot.latitude, spot.longitude)
                loadReports(id)
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Spot '${spot.name}' cargado con éxito.")
            }.onFailure {
                _errorMessage.value = "Error al cargar el spot: ${it.localizedMessage}"
                com.istudio.crsurfguide.ui.debug.LogBuffer.e("SurfViewModel", "Fallo al cargar spot ID: $id", it)
            }
            _isLoading.value = false
        }
    }

    private fun loadReports(spotId: String) {
        viewModelScope.launch {
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Cargando reportes para spot: $spotId")
            reportRepository.getReportsForSpot(spotId).collect {
                _spotReports.value = it
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Recibidos ${it.size} reportes.")
            }
        }
    }

    fun uploadReport(spotId: String, imageUri: Uri, comment: String, waveHeight: String, windCondition: String) {
        val user = _userProfile.value ?: return
        val report = SpotReport(
            spotId = spotId,
            userId = user.uid,
            userName = user.name,
            userProfileImageUrl = user.profileImageUrl,
            comment = comment,
            waveHeight = waveHeight,
            windCondition = windCondition
        )
        
        viewModelScope.launch {
            _isLoading.value = true
            reportRepository.uploadReport(report, imageUri).onSuccess {
                _errorMessage.value = "Reporte subido con éxito"
            }.onFailure {
                _errorMessage.value = "Error al subir reporte: ${it.localizedMessage}"
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
        val uid = forcedFakeUid ?: authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("SurfViewModel", "Alternando favorito para spot: $spotId (User: $uid)")
            userRepository.toggleFavoriteSpot(uid, spotId)
        }
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
