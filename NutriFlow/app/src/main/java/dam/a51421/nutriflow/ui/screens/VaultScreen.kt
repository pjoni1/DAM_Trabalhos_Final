package dam.a51421.nutriflow.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import dam.a51421.nutriflow.data.model.MediaEntry
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import androidx.compose.ui.res.stringResource
import dam.a51421.nutriflow.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(viewModel: NutriFlowViewModel) {
    val mediaEntries by viewModel.mediaEntries.collectAsState()
    var selectedTab by remember { mutableStateOf("Evolution") } // "Evolution" or "Food"
    var activeZoomImage by remember { mutableStateOf<MediaEntry?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.addMediaEntry(it, selectedTab)
            }
        }
    )

    val filteredMedia = mediaEntries.filter { it.category == selectedTab }

    // Agrupamento por mês (ex: "Maio 2026")
    val groupedMedia = filteredMedia
        .sortedByDescending { it.date }
        .groupBy { mediaEntry ->
            val sdf = SimpleDateFormat("MMMM yyyy", Locale("pt"))
            sdf.format(Date(mediaEntry.date)).replaceFirstChar { it.uppercase() }
        }

    Scaffold(
        floatingActionButton = {
            if (filteredMedia.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        photoPickerLauncher.launch("image/*")
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = "Adicionar Foto")
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Seletor de Abas (Tabs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == "Evolution",
                    onClick = { selectedTab = "Evolution" },
                    label = { Text(stringResource(R.string.physical_evolution)) },
                    leadingIcon = { Icon(Icons.Default.FitnessCenter, null) },
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = selectedTab == "Food",
                    onClick = { selectedTab = "Food" },
                    label = { Text(stringResource(R.string.meal_photos)) },
                    leadingIcon = { Icon(Icons.Default.Restaurant, null) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Cabeçalho da categoria ativa
            Text(
                text = stringResource(if (selectedTab == "Evolution") R.string.evolution_vault else R.string.meal_diary),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(if (selectedTab == "Evolution") R.string.evolution_vault_subtitle else R.string.meal_diary_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(16.dp))

            if (filteredMedia.isEmpty()) {
                // Estado Vazio (Empty State)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (selectedTab == "Evolution") Icons.Default.AddAPhoto else Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(if (selectedTab == "Evolution") R.string.empty_evolution else R.string.empty_meal),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(if (selectedTab == "Evolution") R.string.empty_evolution_subtitle else R.string.empty_meal_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = {
                                photoPickerLauncher.launch("image/*")
                            },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.add_first_photo))
                        }
                    }
                }
            } else {
                // Grelha de fotos organizada por meses
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    groupedMedia.forEach { (month, list) ->
                        // Cabeçalho de Mês
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Column(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
                                Text(
                                    text = month,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                            }
                        }

                        // Imagens pertencentes ao mês
                        items(list) { media ->
                            MediaCard(
                                media = media,
                                onClick = { activeZoomImage = media },
                                onDelete = { viewModel.removeMediaEntry(media.id) }
                            )
                        }
                    }
                    
                    // Spacer final para não sobrepor o FAB
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Lightbox / Visualizador de Imagem Expandida (Premium Component)
    if (activeZoomImage != null) {
        Dialog(
            onDismissRequest = { activeZoomImage = null },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { activeZoomImage = null }
            ) {
                AsyncImage(
                    model = activeZoomImage!!.filePath,
                    contentDescription = "Visualização Expandida",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )

                // Botão de Fechar
                IconButton(
                    onClick = { activeZoomImage = null },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                }

                // Legenda de Data
                Text(
                    text = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt")).format(Date(activeZoomImage!!.date)),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun MediaCard(
    media: MediaEntry,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd 'de' MMMM", Locale("pt")) }
    val formattedDate = dateFormat.format(Date(media.date))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = media.filePath,
                contentDescription = "Imagem do Cofre",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Botão semi-transparente no canto superior direito para Apagar
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Apagar Imagem",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Barra inferior com gradiente e data
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = formattedDate,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}