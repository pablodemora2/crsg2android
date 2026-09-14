package com.istudio.crsurfguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.istudio.crsurfguide.ui.surf.SurfViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModernMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    
                    val spotIdFromIntent = intent.getStringExtra("spotId")
                    LaunchedEffect(spotIdFromIntent) {
                        if (!spotIdFromIntent.isNullOrEmpty()) {
                            navController.navigate("surf_detail/$spotIdFromIntent")
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
                            ProfileScreen(viewModel = profileViewModel)
                        }
                    }
                }
            }
        }
    }
}
