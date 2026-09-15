package com.istudio.crsurfguide.ui.gallery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.istudio.crsurfguide.ui.components.SurfTopBar

@Composable
fun GalleryScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SurfTopBar(
                title = "Galería de Fotos",
                navController = navController,
                showBackButton = true
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mock de fotos para la galería
            items(12) { index ->
                Card(
                    modifier = Modifier.aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    AsyncImage(
                        model = "https://picsum.photos/400/400?random=$index",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
