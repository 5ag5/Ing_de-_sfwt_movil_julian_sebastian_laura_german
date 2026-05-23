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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.di.AppModule
import com.tsdc.vinilos.ui.shared.constants.ColorConstants
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.viewmodels.ArtistViewModel
import com.tsdc.vinilos.ui.viewmodels.CollectorViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArtistScreenPreview() {
    HomeScreen(
        albumViewModel = com.tsdc.vinilos.ui.viewmodels.AlbumViewModel(AppModule.getAlbumsUseCase),
        artistViewModel = ArtistViewModel(AppModule.getArtistsUseCase),
        collectorViewModel = CollectorViewModel(AppModule.getCollectorsUseCase),
        initialTab = 2,
        onAlbumClick = {},
        onArtistClick = {}
    )
}

@Composable
fun ArtistScreen(viewModel: ArtistViewModel, onArtistClick: (Int) -> Unit = {}) {
    val artists by viewModel.artists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadArtists()
    }

    val filteredArtists = remember(artists, searchQuery) {
        if (searchQuery.isBlank()) {
            artists
        } else {
            artists.filter { artist ->
                artist.name.contains(searchQuery, ignoreCase = true) ||
                    artist.description.contains(searchQuery, ignoreCase = true)
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
                
                Text(
                    text = "Artists",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191B24)
                )
            }

         
        }

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .testTag(UiTestTags.ARTIST_SEARCH_FIELD),
            placeholder = {
                Text("Search artists...", color = ColorConstants.navUnselected, fontSize = 15.sp)
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
                unfocusedIndicatorColor = Color(0xFFB4BAC7),
                focusedIndicatorColor = Color(0xFF2B35BD),
                cursorColor = Color(0xFF2B35BD)
            )
        )

        Button(
            onClick = { 
                // Favorite artists feature to be implemented
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E3FAE)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "My favorite Artists",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = "FEATURED ARTISTS",
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
            color = ColorConstants.navUnselected,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        when {
            isLoading -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(5) { ShimmerArtistItem() }
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
                        contentDescription = "Error al cargar artistas",
                        tint = ColorConstants.navUnselected,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No se pudieron cargar los artistas",
                        color = Color(0xFF1A1A2E),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = error ?: "", color = ColorConstants.navUnselected, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.loadArtists() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text(text = "Reintentar", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            filteredArtists.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "No hay artistas disponibles"
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
                    items(filteredArtists, key = { it.id }) { artist ->
                        ArtistItem(artist = artist, onClick = { onArtistClick(artist.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ShimmerArtistItem() {
    val infiniteTransition = rememberInfiniteTransition(label = "artist_shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "artist_shimmer_alpha"
    )
    val shimmerColor = Color(0xFFE8EBF2).copy(alpha = alpha)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(shimmerColor)
            )
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
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
                        .fillMaxWidth(0.65f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerColor)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.18f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(shimmerColor)
            )
        }
    }
}