package com.istudio.crsurfguide.ui.surf

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfDetailScreen(
    spotId: String,
    viewModel: SurfViewModel,
    onBackClick: () -> Unit,
    onMapClick: () -> Unit
) {
    val spot by viewModel.selectedSpot.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(spotId) {
        viewModel.loadSpotById(spotId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(spot?.name ?: "Detalles") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    spot?.let { s ->
                        val isFavorite = userProfile?.favoriteSurfSpotIds?.contains(s.id) == true
                        IconButton(onClick = { viewModel.toggleFavorite(s.id) }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (spot != null) {
                val currentSpot = spot!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (currentSpot.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = currentSpot.imageUrl,
                            contentDescription = currentSpot.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = currentSpot.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = currentSpot.zone,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ConditionCard(
                                title = "Tipo de Ola",
                                value = currentSpot.waveType,
                                icon = Icons.Default.Waves
                            )
                            ConditionCard(
                                title = "Nivel",
                                value = currentSpot.difficulty,
                                icon = Icons.Default.Star
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ConditionCard(
                                title = "Viento",
                                value = currentSpot.windDirection,
                                icon = Icons.Default.Info
                            )
                            ConditionCard(
                                title = "Marea",
                                value = currentSpot.tideHeight,
                                icon = Icons.Default.Water
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = if (currentSpot.detailedDescription.isNotEmpty()) 
                                currentSpot.detailedDescription else currentSpot.description,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onMapClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver en el Mapa")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionCard(title: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = title, style = MaterialTheme.typography.labelSmall)
            Text(text = value.ifEmpty { "N/A" }, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
