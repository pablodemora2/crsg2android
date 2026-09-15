package com.istudio.crsurfguide.ui.surf

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.istudio.crsurfguide.domain.model.SpotReport
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfDetailScreen(
    spotId: String,
    viewModel: SurfViewModel,
    onBackClick: () -> Unit,
    onMapClick: () -> Unit,
    onAddPhotoClick: (String) -> Unit,
    onChatClick: (String, String) -> Unit
) {
    val spot by viewModel.selectedSpot.collectAsState()
    val weather by viewModel.surfWeather.collectAsState()
    val spotReports by viewModel.spotReports.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(spotId) {
        viewModel.loadSpotById(spotId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(spot?.name ?: "Detalles") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    spot?.let { s ->
                        IconButton(onClick = { onChatClick(s.id, s.name) }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat del Spot")
                        }
                        val isFavorite = favoriteIds.contains(s.id)
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
            if (isLoading && spot == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (spot == null) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No se pudo cargar la información del spot.")
                    Button(onClick = { viewModel.loadSpotById(spotId) }) {
                        Text("Reintentar")
                    }
                }
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

                        Text(
                            text = "Condiciones en Tiempo Real",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ConditionCard(
                                title = "Altura Ola",
                                value = if (weather != null) "${weather?.waveHeight}m" else "Cargando...",
                                icon = Icons.Default.Waves
                            )
                            ConditionCard(
                                title = "Periodo",
                                value = if (weather != null) "${weather?.wavePeriod}s" else "Cargando...",
                                icon = Icons.Default.Timer
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ConditionCard(
                                title = "Viento",
                                value = if (weather != null) "${weather?.windSpeed}km/h" else "Cargando...",
                                icon = Icons.Default.Air
                            )
                            ConditionCard(
                                title = "Temp.",
                                value = if (weather != null) "${weather?.temperature}°C" else "Cargando...",
                                icon = Icons.Default.Thermostat
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Divider()
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Información Técnica",
                            style = MaterialTheme.typography.titleLarge
                        )
                        
                        ConditionRow(label = "Marea Ideal", value = currentSpot.bestTide)
                        ConditionRow(label = "Dificultad", value = currentSpot.difficulty)

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
                        
                        // Sección de Reportes de la Comunidad
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reportes de la Comunidad",
                                style = MaterialTheme.typography.titleLarge
                            )
                            TextButton(onClick = { onAddPhotoClick(currentSpot.id) }) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Añadir")
                            }
                        }
                        
                        if (spotReports.isEmpty()) {
                            Text(
                                text = "Sé el primero en subir una foto de hoy",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        } else {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(spotReports) { report ->
                                    SpotReportItem(report)
                                }
                            }
                        }

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
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ConditionRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SpotReportItem(report: SpotReport) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(280.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = report.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = report.userProfileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = report.userName, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Ola: ${report.waveHeight} | ${report.windCondition}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = report.comment,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
