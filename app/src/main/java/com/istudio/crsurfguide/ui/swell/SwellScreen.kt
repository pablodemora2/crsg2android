package com.istudio.crsurfguide.ui.swell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.istudio.crsurfguide.ui.components.SurfTopBar

@Composable
fun SwellScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SurfTopBar(
                title = "Swell & Pronóstico",
                navController = navController,
                showBackButton = true
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Pronóstico General de Costa Rica",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            
            items(5) { index ->
                SwellForecastCard(day = "Día ${index + 1}")
            }
        }
    }
}

@Composable
fun SwellForecastCard(day: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = day, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Altura Ola: 1.5m - 2.0m")
                Text("Periodo: 12s")
            }
            Text("Viento: 15km/h Offshore", color = MaterialTheme.colorScheme.primary)
        }
    }
}
