package com.istudio.crsurfguide.ui.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val profileState by viewModel.profileState.collectAsState()
    val isUpdating by viewModel.isUpdating.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Perfil") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = profileState) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ProfileState.Success -> {
                    val profile = state.profile
                    var name by remember { mutableStateOf(profile.name) }
                    var surfLevel by remember { mutableStateOf(profile.surfLevel) }
                    var favoriteSpot by remember { mutableStateOf(profile.favoriteSpot) }

                    val galleryLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri ->
                        uri?.let { viewModel.uploadImage(it) }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { galleryLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (profile.profileImageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = profile.profileImageUrl,
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Subir foto",
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            
                            if (isUpdating) {
                                CircularProgressIndicator(modifier = Modifier.size(120.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = surfLevel,
                            onValueChange = { surfLevel = it },
                            label = { Text("Nivel de Surf") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = favoriteSpot,
                            onValueChange = { favoriteSpot = it },
                            label = { Text("Spot Favorito") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (isUpdating) {
                            CircularProgressIndicator()
                        } else {
                            Button(
                                onClick = { viewModel.updateProfile(name, surfLevel, favoriteSpot) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Guardar Cambios")
                            }
                        }
                    }
                }
            }
        }
    }
}
