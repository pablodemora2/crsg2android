package com.istudio.crsurfguide.ui.surf

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadSpotPhotoScreen(
    spotId: String,
    viewModel: SurfViewModel,
    onBackClick: () -> Unit,
    onUploadSuccess: () -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var comment by remember { mutableStateOf("") }
    var waveHeight by remember { mutableStateOf("") }
    var windCondition by remember { mutableStateOf("") }
    
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            if (it == "Reporte subido con éxito") {
                onUploadSuccess()
            } else {
                snackbarHostState.showSnackbar(it)
            }
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Reporte de Olas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Button(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Seleccionar Foto")
                    }
                }
            }

            OutlinedTextField(
                value = waveHeight,
                onValueChange = { waveHeight = it },
                label = { Text("Altura de Ola (ej: 1.5m)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = windCondition,
                onValueChange = { windCondition = it },
                label = { Text("Viento (ej: Offshore, Suave)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentarios sobre la sesión") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        val uri = imageUri
                        if (uri != null) {
                            viewModel.uploadReport(spotId, uri, comment, waveHeight, windCondition)
                        } else {
                            // Mostrar error de imagen faltante si fuera necesario
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = imageUri != null
                ) {
                    Text("Publicar Reporte")
                }
            }
        }
    }
}
