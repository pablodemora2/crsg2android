package com.istudio.crsurfguide.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
