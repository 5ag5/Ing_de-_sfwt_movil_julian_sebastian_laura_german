package com.tsdc.vinilos.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.di.AppModule
import com.tsdc.vinilos.ui.shared.constants.ColorConstants
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.viewmodels.CollectorViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectorScreenPreview() {
    HomeScreen(
        albumViewModel = com.tsdc.vinilos.ui.viewmodels.AlbumViewModel(AppModule.getAlbumsUseCase),
        artistViewModel = com.tsdc.vinilos.ui.viewmodels.ArtistViewModel(AppModule.getArtistsUseCase),
        collectorViewModel = CollectorViewModel(AppModule.getCollectorsUseCase),
        initialTab = 3,
        onAlbumClick = {}
    )
}

@Composable
fun CollectorScreen(viewModel: CollectorViewModel, onCollectorClick: (Int) -> Unit = {}) {
    val collectors by viewModel.collectors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadCollectors()
    }

    val filteredCollectors = remember(collectors, searchQuery) {
        if (searchQuery.isBlank()) {
            collectors
        } else {
            collectors.filter { collector ->
                collector.name.contains(searchQuery, ignoreCase = true) ||
                    collector.email.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2B35BD)
                    )
                }
                Text(
                    text = "Collectors",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191B24)
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color(0xFF191B24)
                )
            }
        }

        val searchFieldDescription = if (searchQuery.isBlank()) {
            "Buscar coleccionistas"
        } else {
            "Buscar coleccionistas, $searchQuery"
        }
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .testTag(UiTestTags.COLLECTOR_SEARCH_FIELD)
                .semantics { contentDescription = searchFieldDescription },
            label = {
                Text(
                    text = "Buscar coleccionistas",
                    color = ColorConstants.navUnselected,
                    fontSize = 15.sp
                )
            },
            placeholder = {
                Text(
                    text = "Nombre o correo...",
                    color = ColorConstants.navUnselected,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = ColorConstants.navUnselected
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF0F2F6),
                focusedContainerColor = Color(0xFFF0F2F6),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color(0xFF191B24),
                unfocusedTextColor = Color(0xFF191B24),
                cursorColor = Color(0xFF2B35BD)
            )
        )

        Text(
            text = "COMMUNITY",
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
            color = Color(0xFF2B35BD),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Text(
            text = "CURATORS & ENTHUSIASTS",
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 0.dp),
            color = ColorConstants.navUnselected,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(4) { ShimmerCollectorItem() }
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error al cargar coleccionistas",
                        tint = ColorConstants.navUnselected,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No se pudieron cargar los coleccionistas",
                        color = Color(0xFF1A1A2E),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = error ?: "", color = ColorConstants.navUnselected, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.loadCollectors() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text(text = "Reintentar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            filteredCollectors.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "No hay coleccionistas disponibles"
                        else "Sin resultados para \"$searchQuery\"",
                        color = Color(0xFF1A1A2E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isBlank()) "Intenta recargar la lista"
                        else "Prueba con otra busqueda",
                        color = ColorConstants.navUnselected,
                        fontSize = 14.sp
                    )
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredCollectors, key = { it.id }) { collector ->
                        CollectorItem(
                            collector = collector,
                            onClick = { onCollectorClick(collector.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShimmerCollectorItem() {
    val infiniteTransition = rememberInfiniteTransition(label = "collector_shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "collector_shimmer_alpha"
    )
    val shimmerColor = Color(0xFFE5E5EA).copy(alpha = alpha)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(shimmerColor)
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerColor)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerColor)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmerColor)
            )
        }
    }
}
