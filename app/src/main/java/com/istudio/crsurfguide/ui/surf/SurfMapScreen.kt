package com.istudio.crsurfguide.ui.surf

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfMapScreen(
    viewModel: SurfViewModel,
    onBackClick: () -> Unit
) {
    val spots by viewModel.spots.collectAsState()
    
    // Posición inicial del mapa (Costa Rica)
    val costaRica = LatLng(9.7489, -83.7534)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(costaRica, 7f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de Spots") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←") // O un ícono de flecha real
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState
        ) {
            spots.forEach { spot ->
                Marker(
                    state = MarkerState(position = LatLng(spot.latitude, spot.longitude)),
                    title = spot.name,
                    snippet = spot.zone
                )
            }
        }
    }
}
