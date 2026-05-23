package com.tsdc.vinilos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tsdc.vinilos.domain.models.Artist
import com.tsdc.vinilos.ui.shared.constants.ColorConstants
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.ArtistDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArtistDetailScreenPreview() {
    VinilosTheme {
        ArtistDetailContent(
            artist = Artist(
                id = 101,
                name = "Miles Davis",
                image = "https://upload.wikimedia.org/wikipedia/commons/f/fa/Miles_Davis_by_Palumbo.jpg",
                description = "A visionary trumpeter, bandleader, and composer, " +
                    "Miles Davis stands as one of the most influential figures in the " +
                    "history of jazz and 20th-century music.",
                birthDate = Date()
            ),
            isLoading = false,
            error = null,
            isFavorite = false,
            onRetry = {},
            onBack = {},
            onToggleFavorite = {}
        )
    }
}

@Composable
fun ArtistDetailScreen(
    viewModel: ArtistDetailViewModel,
    artistId: Int,
    onBack: () -> Unit
) {
    val artist by viewModel.artist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadArtist(artistId)
    }

    ArtistDetailContent(
        artist = artist,
        isLoading = isLoading,
        error = error,
        isFavorite = isFavorite,
        onRetry = { viewModel.loadArtist(artistId) },
        onBack = onBack,
        onToggleFavorite = { viewModel.toggleFavorite(artistId) }
    )
}

@Composable
private fun ArtistDetailContent(
    artist: Artist?,
    isLoading: Boolean,
    error: String?,
    isFavorite: Boolean,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(UiTestTags.ARTIST_DETAIL_ROOT)
            .fillMaxSize()
            .background(Color(0xFFF2F2F6))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF2B35BD)
                    )
                }
                Text(
                    text = "Artists",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191B24)
                )
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.testTag(UiTestTags.ARTIST_DETAIL_FAVORITE)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos"
                    else "Agregar a favoritos",
                    tint = if (isFavorite) Color(0xFFE53935) else Color(0xFF191B24)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF2B35BD))
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (error != null) {
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = Color(0xFFB00020),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reintentar", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            val currentArtist = artist ?: Artist(
                id = -1,
                name = "Cargando...",
                image = "",
                description = "Esperando datos del backend.",
                birthDate = Date()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.55f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE1E4F2))
                    .semantics(mergeDescendants = true) {
                        contentDescription = "Artista destacado ${currentArtist.name}"
                    }
            ) {
                AsyncImage(
                    model = currentArtist.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.0f to Color.Transparent,
                                    0.45f to Color.Black.copy(alpha = 0.25f),
                                    1.0f to Color.Black.copy(alpha = 0.88f)
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "FEATURED ARTIST",
                        fontSize = 12.sp,
                        letterSpacing = 1.4.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorConstants.accentGoldAccessible
                    )
                    Text(
                        text = currentArtist.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = currentArtist.description,
                fontSize = 15.sp,
                color = Color(0xFF4D4D4D),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE1E4F2)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FA)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "METADATA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF2B35BD),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    DetailRowSpread(
                        label = "ID",
                        value = currentArtist.id.toString()
                    )
                    DetailRowSpread(
                        label = "Fecha de nacimiento",
                        value = currentArtist.birthDate.toFormattedDate()
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color(0xFF2B35BD)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "View Albums",
                    color = Color(0xFF2B35BD),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onToggleFavorite,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B35BD)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .testTag(UiTestTags.ARTIST_DETAIL_FOLLOW)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (isFavorite) "Following Artist" else "Follow Artist",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DetailRowSpread(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF4D4D4D)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E)
        )
    }
}

private fun Date.toFormattedDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(this)
}
