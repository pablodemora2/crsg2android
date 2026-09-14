package com.istudio.crsurfguide.ui.surf

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Map
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfListScreen(
    viewModel: SurfViewModel,
    onProfileClick: () -> Unit,
    onMapClick: () -> Unit,
    onSpotClick: (String) -> Unit
) {
    val spots by viewModel.spots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Costa Rica Surf Guide") },
                actions = {
                    IconButton(onClick = { viewModel.toggleShowOnlyFavorites() }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            tint = if (showOnlyFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onMapClick) {
                Icon(imageVector = Icons.Default.Map, contentDescription = "Ver Mapa")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(spots) { spot ->
                        val isFavorite = favoriteIds.contains(spot.id)
                        SurfSpotItem(
                            spot = spot,
                            isFavorite = isFavorite,
                            onFavoriteClick = { viewModel.toggleFavorite(spot.id) },
                            onClick = { onSpotClick(spot.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SurfSpotItem(
    spot: SurfSpot,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = spot.name, style = MaterialTheme.typography.titleLarge)
                Text(text = spot.zone, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = spot.description, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}
