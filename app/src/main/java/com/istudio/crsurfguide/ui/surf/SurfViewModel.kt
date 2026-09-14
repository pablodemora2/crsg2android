package com.istudio.crsurfguide.ui.surf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.repository.SurfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurfViewModel @Inject constructor(
    private val surfRepository: SurfRepository
) : ViewModel() {

    private val _spots = MutableStateFlow<List<SurfSpot>>(emptyList())
    val spots: StateFlow<List<SurfSpot>> = _spots.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSpots()
    }

    fun loadSpots() {
        viewModelScope.launch {
            _isLoading.value = true
            surfRepository.getSurfSpots().collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _spots.value = it
                }
            }
        }
    }

    fun loadSpotsByZone(zone: String) {
        viewModelScope.launch {
            _isLoading.value = true
            surfRepository.getSpotsByZone(zone).collect { result ->
                _isLoading.value = false
                result.onSuccess {
                    _spots.value = it
                }
            }
        }
    }
}
