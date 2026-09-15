package com.istudio.crsurfguide.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.istudio.crsurfguide.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    onNavigateNext: () -> Unit
) {
    // Simular un tiempo de carga y navegar automáticamente
    LaunchedEffect(Unit) {
        delay(2500)
        onNavigateNext()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo (reutilizando el recurso existente)
        Image(
            painter = painterResource(id = R.drawable.im_splash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay oscuro para legibilidad
        Surface(
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxSize()
        ) {}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_eagle),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Costa Rica Surf Guide",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Modern Edition",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }

        // Botón de QA solicitado en la parte inferior
        Button(
            onClick = onNavigateNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(text = "🚀 Nuevo flujo: Iniciar acá nueva experiencia")
        }
    }
}
