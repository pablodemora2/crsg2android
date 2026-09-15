package com.istudio.crsurfguide.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfTopBar(
    title: String,
    navController: NavController,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = { navController.popBackStack() },
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            }
        },
        actions = {
            actions()
            SurfOverflowMenu(navController)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun SurfOverflowMenu(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Main / Home") },
                onClick = { 
                    expanded = false
                    navController.navigate("surf_list") {
                        launchSingleTop = true
                        popUpTo("surf_list") { inclusive = true }
                    }
                },
                leadingIcon = { Icon(Icons.Default.Home, null) }
            )
            DropdownMenuItem(
                text = { Text("Listas de Olas") },
                onClick = { 
                    expanded = false
                    navController.navigate("surf_list")
                },
                leadingIcon = { Icon(Icons.Default.Waves, null) }
            )
            DropdownMenuItem(
                text = { Text("Mapa Interactivo") },
                onClick = { 
                    expanded = false
                    navController.navigate("surf_map")
                },
                leadingIcon = { Icon(Icons.Default.Map, null) }
            )
            DropdownMenuItem(
                text = { Text("Galerías Genéricas") },
                onClick = { 
                    expanded = false
                    navController.navigate("gallery")
                },
                leadingIcon = { Icon(Icons.Default.PhotoLibrary, null) }
            )
            DropdownMenuItem(
                text = { Text("Swell (Pronóstico)") },
                onClick = { 
                    expanded = false
                    navController.navigate("swell")
                },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, null) }
            )
            DropdownMenuItem(
                text = { Text("Chat Comunitario") },
                onClick = { 
                    expanded = false
                    navController.navigate("spot_chat/general/General")
                },
                leadingIcon = { Icon(Icons.Default.Chat, null) }
            )
            
            HorizontalDivider()

            DropdownMenuItem(
                text = { Text("Perfil de Usuario") },
                onClick = { 
                    expanded = false
                    navController.navigate("profile")
                },
                leadingIcon = { Icon(Icons.Default.Person, null) }
            )
            DropdownMenuItem(
                text = { Text("Login / Registro") },
                onClick = { 
                    expanded = false
                    navController.navigate("auth") {
                        popUpTo(0)
                    }
                },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) }
            )
        }
    }
}
