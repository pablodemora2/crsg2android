package com.istudio.crsurfguide

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.istudio.crsurfguide.ui.auth.AuthScreen
import com.istudio.crsurfguide.ui.auth.AuthViewModel
import com.istudio.crsurfguide.ui.profile.ProfileScreen
import com.istudio.crsurfguide.ui.profile.ProfileViewModel
import com.istudio.crsurfguide.ui.surf.SurfDetailScreen
import com.istudio.crsurfguide.ui.surf.SurfListScreen
import com.istudio.crsurfguide.ui.surf.SurfMapScreen
import com.istudio.crsurfguide.ui.surf.SurfMapScreen
import com.istudio.crsurfguide.ui.surf.SurfViewModel
import com.istudio.crsurfguide.ui.debug.LogBuffer
import com.istudio.crsurfguide.ui.debug.DebugHud
import com.istudio.crsurfguide.ui.theme.ThemeManager
import com.istudio.crsurfguide.ui.theme.AppTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModernMainActivity : ComponentActivity() {
    
    private var pendingSpotId by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        LogBuffer.d("ModernMainActivity", "onCreate iniciado. Verificando inyecciones y extras...")
        super.onCreate(savedInstanceState)
        
        pendingSpotId = intent.getStringExtra("spotId")
        LogBuffer.d("ModernMainActivity", "Extra spotId obtenido: $pendingSpotId")

        setContent {
            val currentTheme by ThemeManager.currentTheme.collectAsState()
            val useDarkTheme = when (currentTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            MaterialTheme(
                colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                    Surface(color = MaterialTheme.colorScheme.background, modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                        val navController = rememberNavController()
                        
                        LaunchedEffect(pendingSpotId) {
                            pendingSpotId?.let { spotId ->
                                LogBuffer.d("ModernMainActivity", "Navegando a través de Deep Link al spot: $spotId")
                                navController.navigate("surf_detail/$spotId")
                                pendingSpotId = null
                            }
                        }
                    
                    NavHost(navController = navController, startDestination = "auth") {
                        composable("auth") {
                            val authViewModel = hiltViewModel<AuthViewModel>()
                            AuthScreen(
                                viewModel = authViewModel,
                                onAuthSuccess = {
                                    navController.navigate("surf_list") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("surf_list") {
                            val surfViewModel = hiltViewModel<SurfViewModel>()
                            SurfListScreen(
                                viewModel = surfViewModel,
                                onProfileClick = {
                                    navController.navigate("profile")
                                },
                                onMapClick = {
                                    navController.navigate("surf_map")
                                },
                                onSpotClick = { spotId ->
                                    navController.navigate("surf_detail/$spotId")
                                }
                            )
                        }
                        composable("surf_map") {
                            val surfViewModel = hiltViewModel<SurfViewModel>()
                            SurfMapScreen(
                                viewModel = surfViewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onSpotClick = { spotId ->
                                    navController.navigate("surf_detail/$spotId")
                                }
                            )
                        }
                        composable("surf_detail/{spotId}") { backStackEntry ->
                            val spotId = backStackEntry.arguments?.getString("spotId") ?: ""
                            val surfViewModel = hiltViewModel<SurfViewModel>()
                            SurfDetailScreen(
                                spotId = spotId,
                                viewModel = surfViewModel,
                                onBackClick = { navController.popBackStack() },
                                onMapClick = { navController.navigate("surf_map") }
                            )
                        }
                        composable("profile") {
                            val profileViewModel = hiltViewModel<ProfileViewModel>()
                            ProfileScreen(
                                viewModel = profileViewModel,
                                onLogout = {
                                    navController.navigate("auth") {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }
                    }
                    }
                    
                    // In-App Diagnóstico Terminal HUD Flotante
                    DebugHud()
                }
            }
        }
        LogBuffer.d("ModernMainActivity", "setContent cargado con éxito en UI.")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingSpotId = intent.getStringExtra("spotId")
    }
}
