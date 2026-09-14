package com.istudio.crsurfguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.istudio.crsurfguide.ui.auth.AuthScreen
import com.istudio.crsurfguide.ui.auth.AuthViewModel
import com.istudio.crsurfguide.ui.profile.ProfileScreen
import com.istudio.crsurfguide.ui.profile.ProfileViewModel
import com.istudio.crsurfguide.ui.surf.SurfListScreen
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
                                }
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
