package com.istudio.crsurfguide.ui.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.istudio.crsurfguide.ui.theme.AppTheme
import com.istudio.crsurfguide.ui.theme.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()
    val isUpdating by viewModel.isUpdating.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil Social") },
                actions = {
                    IconButton(onClick = {
                        viewModel.signOut()
                        onLogout()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = profileState) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadProfile() }) {
                            Text("Reintentar")
                        }
                    }
                }
                is ProfileState.Success -> {
                    val profile = state.profile
                    var name by remember { mutableStateOf(profile.name) }
                    var bio by remember { mutableStateOf(profile.bio) }
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
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Foto de Perfil
                        Box(
                            modifier = Modifier
                                .size(100.dp)
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
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            
                            if (isUpdating) {
                                CircularProgressIndicator(modifier = Modifier.size(100.dp))
                            }
                        }

                        Text(
                            text = profile.email,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Estadísticas Sociales
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(label = "Reportes", value = profile.totalReports.toString())
                            StatItem(label = "Favoritos", value = profile.favoriteSurfSpotIds.size.toString())
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre Público") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Biografía / Sobre mí") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
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
                            label = { Text("Spot Predilecto") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Selector de Temas Integrado
                        Text("Personalización", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
                        val context = LocalContext.current
                        val currentTheme by ThemeManager.currentTheme.collectAsState()
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppTheme.values().forEach { theme ->
                                FilterChip(
                                    selected = currentTheme == theme,
                                    onClick = { ThemeManager.setTheme(context, theme) },
                                    label = { Text(theme.name) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (isUpdating) {
                            CircularProgressIndicator()
                        } else {
                            Button(
                                onClick = { viewModel.updateProfile(name, bio, surfLevel, favoriteSpot) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Actualizar Perfil")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}
