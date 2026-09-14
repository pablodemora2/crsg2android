package com.istudio.crsurfguide.ui.surf

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import com.istudio.crsurfguide.domain.model.SurfSpot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfListScreen(
    viewModel: SurfViewModel,
    onProfileClick: () -> Unit
) {
    val spots by viewModel.spots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Costa Rica Surf Guide") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Perfil"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(spots) { spot ->
                        SurfSpotItem(spot)
                    }
                }
            }
        }
    }
}

@Composable
fun SurfSpotItem(spot: SurfSpot) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = spot.name, style = MaterialTheme.typography.titleLarge)
            Text(text = spot.zone, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = spot.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
